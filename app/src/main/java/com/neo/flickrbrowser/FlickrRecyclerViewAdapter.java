package com.neo.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
//    field stores the photos list.
    private List<Photo> mPhotosList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photosList, Context context) {
        mContext = context;
        mPhotosList = photosList;
    }

    @NonNull
    @Override
//    inflates a view from the browse xml layout
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Called by te layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view Requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

//        method is used by recyclierview when it wants new data to be stored in a viewholder so that it can be displayed
//        called when layout manager wants new data in an existing row
    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {

//        if statement is executed if no photos in flickr database match our search
        if((mPhotosList == null) || (mPhotosList.size() == 0)){
            holder.thumbanail.setImageResource(R.drawable.placeholder);
            holder.title.setText("No pics match your search.\n\nUse the search icon to search for your desired photo");
        }
        else{
            Photo photoItem = mPhotosList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " --> " + position);
//        with is used to create a picasso object that c and can only be created once # its static, .load loads an image from a url and thumbnail stored in image obj
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.broken_image)
                    .placeholder(R.drawable.placeholder)
//                .into is where the downloaded image is stored into the widget in the view holder
                    .into(holder.thumbanail);

//        puts title into the text view
            holder.title.setText(photoItem.getTitle());

        }


    }

//    method below is called by the recyclerviewadapter to check if there's any item to display
    @Override
    public int getItemCount() {
//        returns the number of photos in the list. using tenary operation if true it returns list of photos size and if false it returns "1"
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size() : 1);
    }

// provides adapter with new list of photos
    void loadNewData(List<Photo> newPhotos){
        mPhotosList = newPhotos;
//        tells recyclierView dat data has changed and hence loads new data # refresh
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.get(position) : null);
    }


    //    the inner class is similar to the viewHolder in the list view used in top10downloader app
//    using a static class makes the inner class to behave like a top level class
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbanail = null;
        TextView title = null;


    public FlickrImageViewHolder(@NonNull View itemView) {
        super(itemView);
        Log.d(TAG, "FlickrImageViewHolder: starts");
        this.thumbanail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.title = (TextView) itemView.findViewById(R.id.title);
    }
}
    
}
