package com.example.denish.interviewexperience;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.Post;

public class PostDetailActivity extends AppCompatActivity {


    private static final String POST_TRANSFER = "POST_TRANSFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Post post =(Post) intent.getSerializableExtra(POST_TRANSFER);
        if(post!=null){
//            Toast.makeText(this, "Pohchi gaya", Toast.LENGTH_SHORT).show();

//            TextView photoTitle = findViewById(R.id.photo_title);
////            photoTitle.setText("Title: "+ photo.getTitle());
//            Resources resources = getResources();
//            String text = resources.getString(R.string.photo_title_text,photo.getTitle());
//            photoTitle.setText(text);
//
//            TextView photoTags = findViewById(R.id.photo_tags);
//            photoTags.setText(resources.getString(R.string.photo_tags_text,photo.getTags()));
//
//            TextView photoAuthor = findViewById(R.id.photo_author);
//            photoAuthor.setText(photo.getAuthor());

        }
    }

}
