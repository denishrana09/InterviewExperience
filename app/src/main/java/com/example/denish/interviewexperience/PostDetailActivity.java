package com.example.denish.interviewexperience;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.denish.interviewexperience.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDetailActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mPostDBRef;
    private ChildEventListener mPostCEListener;

    private static final String POST_TRANSFER = "POST_TRANSFER";
    private static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseDB = FirebaseDatabase.getInstance();
        mPostDBRef = mFirebaseDB.getReference().child("posts");

        Intent intent = getIntent();
        Post post =(Post) intent.getSerializableExtra(POST_TRANSFER);
        final String postKey = (String) intent.getSerializableExtra("postKey");
        String userName = (String) intent.getSerializableExtra("userName");

        if(mPostCEListener == null) {
            Log.d(TAG, "onCreate: called of PostDetailActivity");
            mPostCEListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Post post = dataSnapshot.getValue(Post.class);
                    String postId = dataSnapshot.getKey();
                    if(postId.equals(postKey)) {
                        mPostDBRef.child(postKey).child("views").setValue(post.getViews() + 1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mPostDBRef.addChildEventListener(mPostCEListener);
        }

//        Toast.makeText(this, "Key = " + postKey, Toast.LENGTH_SHORT).show();
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
