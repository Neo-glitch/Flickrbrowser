package com.neo.flickrbrowser;

import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// The activity base class
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

//    method is used to show toolbar and allows activity to decide whether toolbar should have home button or not.
    void activateToolbar(boolean enableHome){
        Log.d(TAG, "activateToolbar: starts");
//        used to get a ref to the actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null){
//            inflates the toolbar from the toolbar xml file
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if(toolbar != null){
//                puts toolbar inplace at top of the screen in the actionBar
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null){
//            depending on boolean passed it enables or disable the actionBar.
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }

    }
}
