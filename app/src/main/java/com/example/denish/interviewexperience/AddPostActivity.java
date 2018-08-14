package com.example.denish.interviewexperience;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.denish.interviewexperience.model.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";
    FirebaseDatabase mFirebaseDB;
    DatabaseReference mPostsDBRef;

    Button addPostButton;
    AutoCompleteTextView title,company,position;
    EditText description;
    String username,email,userid;
    Calendar todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar().setTitle("Add Experience");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow in action bar
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));

        mFirebaseDB = FirebaseDatabase.getInstance();
        mPostsDBRef = mFirebaseDB.getReference().child("posts");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0);
        username = pref.getString("username", "");
        email = pref.getString("email","");
        userid = pref.getString("userId","");

        title = findViewById(R.id.autotv_title);
        company = findViewById(R.id.autotv_company);
        position = findViewById(R.id.autotv_position);
        description = findViewById(R.id.et_description);

        addPostButton = findViewById(R.id.btn_add_post);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayDate = Calendar.getInstance();
                todayDate.set(Calendar.HOUR_OF_DAY, 0);
                //bug : Date not working
                Post model = new Post(title.getText().toString(),company.getText().toString(),
                        position.getText().toString(),
                        description.getText().toString(),userid,todayDate.toString(),0);
                mPostsDBRef.push().setValue(model);
            }
        });

    }


}
