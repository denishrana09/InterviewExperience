package com.example.denish.interviewexperience;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.Post;
import com.example.denish.interviewexperience.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

//    private FirebaseDatabase mFirebaseDB;
//    private DatabaseReference mUsersDBRef;
//    private ChildEventListener mUserCEListener;

    private static final String POST_TRANSFER = "POST_TRANSFER";
    private static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mFirebaseDB = FirebaseDatabase.getInstance();
//        mUsersDBRef = mFirebaseDB.getReference().child("users");
//
//        userIdList = new ArrayList<>();
//        usersList = new ArrayList<>();
//
//        if(mUserCEListener == null){
//            Log.d(TAG, "onCreate: called of PostDetailActivity");
//            mUserCEListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    User user = dataSnapshot.getValue(User.class);
//                    userIdList.add(dataSnapshot.getKey());
//                    usersList.add(user);
//                    Log.d(TAG, "onChildAdded: " + user.getUsername());
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//            mUsersDBRef.addChildEventListener(mUserCEListener);
//        }


        Intent intent = getIntent();
        Post post =(Post) intent.getSerializableExtra(POST_TRANSFER);
        String postKey = (String) intent.getSerializableExtra("postKey");
        String userName = (String) intent.getSerializableExtra("userName");

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
            username.setText(userName);
            username.setLongClickable(false);

        }
    }

}
