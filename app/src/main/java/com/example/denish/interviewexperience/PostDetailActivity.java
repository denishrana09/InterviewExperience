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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Post post =(Post) intent.getSerializableExtra(POST_TRANSFER);
        String postKey = (String) intent.getSerializableExtra("postKey");

        Toast.makeText(this, "Key = " + postKey, Toast.LENGTH_SHORT).show();
        if(post!=null){
            TextView company = findViewById(R.id.tv_detail_company);
            company.setText(post.getCompany());
            company.setLongClickable(false);

            TextView position = findViewById(R.id.tv_detail_position);
            position.setText(post.getPosition());
            position.setLongClickable(false);

            TextView date = findViewById(R.id.tv_detail_date);
            date.setText(post.getDate());
            date.setLongClickable(false);

            TextView description = findViewById(R.id.tv_detail_description);
            description.setText(post.getDescription());
            description.setLongClickable(false);

            TextView username = findViewById(R.id.tv_detail_username);
            username.setText(post.getUserid());
            username.setLongClickable(false);

        }
    }

}
