package com.neo.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


// class gets the json data from the GetRawData class
class GetFlickrjsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrjsonData";

    //    stores list of photo objects from the photo class that we pass out of the json data
    private List<Photo> mPhotolist = null;
    private String mBaseURL;
    private String mLanguage;
    //    param to choose btw matching all the search terms or any of them
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    //    definition of our callback
    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrjsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetFlickrjsonData called");
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }

    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread starts");
//        set to true if we are running on the same thread as the calling process
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);

//        this is used as param so that we can get a callback, param past must be a callback dtype # interface type
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute starts");

        if (mCallBack != null) {
            mCallBack.onDataAvailable(mPhotolist, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
//        super.onPostExecute(photos);
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
//        does what .excute method does, bt not on a diff thread
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return mPhotolist;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri starts");

//        adds the tags to the normal Baseurl inorder to get the link to the needed json file
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
//        SYNTAX ; "matchAll ? "ALL":"ANY"" means it matchAll is true, return all else return any.
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }


    //    executed on the background thread
    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. status = " + status);

//      checks if we have downloaded anything
        if (status == DownloadStatus.OK) {
            mPhotolist = new ArrayList<>();
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
//                    gets one element from the itemsArray i.e a JSONArray
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

//                    used to get the photo in the json
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

//                    used to set the the image size inorder to fill the screen and "_b." is a bigger version of the image
                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotolist.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());

                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing json data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

//        if we are running on same thread ".onDataAvailable method is used."
        if (runningOnSameThread && mCallBack != null) {
//            informs caller that all processing is done - possibly returning null if there is
//            was an error
            mCallBack.onDataAvailable(mPhotolist, status);
        }

        Log.d(TAG, "onDownloadComplete ends");
    }
}
