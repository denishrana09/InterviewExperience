package com.example.denish.interviewexperience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.Company;
import com.example.denish.interviewexperience.model.Position;
import com.example.denish.interviewexperience.model.Post;
import com.example.denish.interviewexperience.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";
    FirebaseDatabase mFirebaseDB;
    DatabaseReference mPostsDBRef,mCompanyDBRef,mPositionDBRef;
    ChildEventListener companyCEListener,positionCEListener;

    Button addPostButton;
    AutoCompleteTextView company,position;
    EditText description;
    String username,email,userid;
    DateFormat dateFormat;
    Date todaysDate;
    List<String> allCompanies,allPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportActionBar().setTitle("Add Experience");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow in action bar
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));

        mFirebaseDB = FirebaseDatabase.getInstance();
        mPostsDBRef = mFirebaseDB.getReference().child("posts");
        mCompanyDBRef = mFirebaseDB.getReference().child("companies");
        mPositionDBRef = mFirebaseDB.getReference().child("positions");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0);
        username = pref.getString("username", "");
        email = pref.getString("email","");
        userid = pref.getString("userId","");

        company = findViewById(R.id.autotv_company);
        allCompanies = getCompanies();
        Log.d(TAG, "onCreate: " + allCompanies);
        final ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, allCompanies);
        company.setAdapter(companyAdapter);

        position = findViewById(R.id.autotv_position);
        allPositions = getPositions();
        Log.d(TAG, "onCreate: " + allPositions);
        final ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, allPositions);
        position.setAdapter(positionAdapter);

        description = findViewById(R.id.et_description);

        addPostButton = findViewById(R.id.btn_add_post);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(company.getText().toString().length()==0){
                    company.setError("Field can not be left blank.");
                }
                else if(position.getText().toString().length()==0){
                    position.setError("Field can not be left blank.");
                }
                else if(description.getText().toString().length()==0){
                    description.setError("Field can not be left blank.");
                }else {
                    todaysDate = new Date();
                    Post model = new Post(company.getText().toString(),
                            position.getText().toString(),
                            description.getText().toString(), userid, dateFormat.format(todaysDate), 0, 0);
                    mPostsDBRef.push().setValue(model);

                    String newCompany = company.getText().toString();
                    String newPosition = position.getText().toString();
                    if (!allCompanies.contains(newCompany)) {
                        Log.d(TAG, "onClick: No Result Found");
                        Company company = new Company(newCompany);
                        mCompanyDBRef.push().setValue(company);
                    }
                    if (!allPositions.contains(newCompany)) {
                        Log.d(TAG, "onClick: No Result Found");
                        Position pos = new Position(newPosition);
                        mPositionDBRef.push().setValue(pos);
                    }
                    company.setText("");
                    position.setText("");
                    description.setText("");
                    Toast.makeText(AddPostActivity.this, "Post added succesfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

    }

    //retrieve company
    List<String> getCompanies(){
        final List<String> companyList = new ArrayList<>();
        if(companyCEListener == null){
            companyCEListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Company company = dataSnapshot.getValue(Company.class);
                    companyList.add(company.getCompany());
                    Log.d(TAG, "onChildAdded: company : " + company.getCompany());
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
            mCompanyDBRef.addChildEventListener(companyCEListener);
        }
//        return companyList.toArray(new String[companyList.size()]);
        return companyList;
    }

    //retrieve position
    List<String> getPositions(){
        final List<String> positionList = new ArrayList<>();
        if(positionCEListener == null){
            positionCEListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Position p = dataSnapshot.getValue(Position.class);
                    positionList.add(p.getPosition());
                    Log.d(TAG, "onChildAdded: position : " + p.getPosition());
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
            mPositionDBRef.addChildEventListener(positionCEListener);
        }
        return positionList;
    }
}
