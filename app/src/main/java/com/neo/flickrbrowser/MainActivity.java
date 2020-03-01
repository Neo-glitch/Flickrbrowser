package com.neo.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends BaseActivity implements GetFlickrjsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        uses method from Base activity, and doesn't activate the actionBar and toolbar.
        activateToolbar(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        creates a new linearlayoutmanager object
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        adds the onItemtouch listener to the recycler view
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

//        creates instamce of recyclierviewadapter and associate it with a recyclierview by using .setAdapter method
        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);


        Log.d(TAG, "onCreate: ends here");
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume starts");
        super.onPostResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");
        if(queryResult.length() > 0){
//        the "this" param means that the object of the type needed as param in this classes is set as first param
            GetFlickrjsonData getFlickrjsonData = new GetFlickrjsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
            getFlickrjsonData.execute(queryResult);
        }

//
        Log.d(TAG, "onPostResume ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search){
            // creates an intent of the SearchActivity class and starts an activity if the icon is clicked
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }

    //        method is gotten from the OnDataAvailable interface
    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");

        if (status == DownloadStatus.OK) {
//            loads data into the recylier view adpater
            mFlickrRecyclerViewAdapter.loadNewData(data);
        } else {
//                download or processing failed
            Log.e(TAG, "onDataAvailable: fail with status" + status);
        }

        Log.d(TAG, "onDataAvailable: ends");
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
//        Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_LONG).show();

//        "this" arg is the Main activity, used here as a context, while the 2nd param is the activity that we want to launch
        Intent intent = new Intent(this, PhotoDetailActivity.class);
//        adds data to the intent, params are key and a value
        intent.putExtra(PHOTO_TRANSFER, mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}


