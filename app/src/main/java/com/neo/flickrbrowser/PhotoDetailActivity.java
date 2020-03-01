package com.neo.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

//        uses method from Base activity class, to enable an actionBar and a toolBar
        activateToolbar(true);

//        "getIntent" is used to get the intent from an already started intent
        Intent intent = getIntent();
//        used to get the photo object using the key used in to get photo stored in MainActivity
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null){
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            Resources resources = getResources();
//            replaces any placeholders specified "photo_title_text" with the actual value
            String text = resources.getString(R.string.photo_title_text, photo.getTitle());
            photoTitle.setText(text);
//            photoTitle.setText("Title: " + photo.getTitle());

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));
//            photoTags.setText("Tags: " + photo.getTags());

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText("Author: " + photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photoImage);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.broken_image)
                    .placeholder(R.drawable.placeholder)
//                .into is where the downloaded image is stored into the widget in the view holder
                    .into(photoImage);
        }



    }

}
