package com.example.denish.interviewexperience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";
    FirebaseDatabase mFirebaseDB;
    DatabaseReference mPostsDBRef;

    Button addPostButton;
    AutoCompleteTextView title,company,position;
    EditText description;
    String username,email,userid;
    DateFormat dateFormat;
    Date todaysDate;

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
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todaysDate = new Date();
                //bug : Date not working
                //feat : check if data already exist for AutoComplete Textview
                Post model = new Post(title.getText().toString(),company.getText().toString(),
                        position.getText().toString(),
                        description.getText().toString(),userid,dateFormat.format(todaysDate),0);
                mPostsDBRef.push().setValue(model);

                title.setText("");
                company.setText("");
                position.setText("");
                description.setText("");
                Toast.makeText(AddPostActivity.this, "Post added succesfully", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }


}
