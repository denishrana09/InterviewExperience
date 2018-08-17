package com.example.denish.interviewexperience;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.Company;
import com.example.denish.interviewexperience.model.Post;
import com.example.denish.interviewexperience.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ManActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mUsersDBRef;
    private DatabaseReference mPostsDBRef;
    private ChildEventListener mUserCEListener;
    private ChildEventListener mPostCEListener;
    ArrayList<String> emailList,userIdList;

    List<Post> mPostsDataItems;
    ArrayList<String> mKeys;

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private String mUsername,isExecuted;

    private NetworkChangeReciever mNetworkChangeReciever;

    private RecyclerViewAdapter mRecyclerViewAdapter;
    public ProgressBar mProgressBar;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddPostActivity.class));
            }
        });

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
//            Log.d(TAG, "onCreate: before Intent of OnBoardingActivity");
            Intent i1 = new Intent(getApplicationContext(),OnBoardingActivity.class);
            startActivity(i1);
//            Log.d(TAG, "onCreate: after Intent of OnBoardingActivity");
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUsersDBRef = mFirebaseDB.getReference().child("users");
        mPostsDBRef = mFirebaseDB.getReference().child("posts");

        //Network Connection
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkChangeReciever = new NetworkChangeReciever();
        registerReceiver(mNetworkChangeReciever, intentFilter);

        emailList = new ArrayList<>();
        userIdList = new ArrayList<>();

        mKeys = new ArrayList<String>();
        mPostsDataItems = new ArrayList<>();
        mProgressBar = findViewById(R.id.content_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        isExecuted = pref.getString("onboard", "");

        if(isExecuted.equals("executed")){
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged: User is not null");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", user.getDisplayName());
                        editor.putString("email",user.getEmail());
                        editor.apply();
                        onSignedInInitialized(user.getDisplayName(),user.getEmail());

                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged: User is null");
                        onSignedOutCleanup();
                        // to-do : own background in AuthUI
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(Arrays.asList(
                                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
            };
        }else{
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                }
            };
        }
    }

    private void onSignedInInitialized(String displayName, final String email) {
        mUsername = displayName;
        if(mUserCEListener == null){
            mUserCEListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    userIdList.add(dataSnapshot.getKey());
                    emailList.add(user.getEmail());
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
            mUsersDBRef.addChildEventListener(mUserCEListener);
        }
//        Log.d(TAG, "onSignedInInitialized: "+ emailList);
        if(emailList.size()!=0 && email!=null && !emailList.contains(email)){
            User user = new User(mUsername,"avatar_person.png",
                    "Hello!!",email);
            String userId = mUsersDBRef.push().getKey();
            mUsersDBRef.child(userId).setValue(user);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("userId", userId.toString());
            editor.apply();
        }
        if(emailList.size()!=0 && email!=null && emailList.contains(email)){
            int userLocation = emailList.indexOf(email);
            String oldUserId = userIdList.get(userLocation);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("userId", oldUserId);
            editor.apply();
        }


        getPosts();

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
    }

    public void getPosts(){
        if(mPostCEListener == null){
            mPostCEListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Post p = dataSnapshot.getValue(Post.class);
                    mPostsDataItems.add(p);
                    String key = dataSnapshot.getKey();
                    mKeys.add(key);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    if(mPostsDataItems.size()!=0)
                        mProgressBar.setVisibility(View.GONE);
//                    Log.d(TAG, "onChildAdded: getPosts: post added : " + p.toString());
//                    Log.d(TAG, "onChildAdded: getPosts: post size : " + mPostsDataItems.size());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue()!=null){
                        String key = dataSnapshot.getKey();
                        int index = mKeys.indexOf(key);
                        Post p = dataSnapshot.getValue(Post.class);
                        mPostsDataItems.set(index,p);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        String key = dataSnapshot.getKey();
                        int index = mKeys.indexOf(key);
                        mPostsDataItems.remove(index);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mPostsDBRef.addChildEventListener(mPostCEListener);
        }
        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.post_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(mPostsDataItems.size()!=0)
            mProgressBar.setVisibility(View.GONE);
        mRecyclerViewAdapter = new RecyclerViewAdapter(this,mPostsDataItems,mUsername);
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Signed In Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //FirebaseAuth
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        if(mPostsDataItems.size()!=0)
            mProgressBar.setVisibility(View.GONE);
        else
            mProgressBar.setVisibility(View.VISIBLE);
        //NetworkChangeReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReciever, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

        //UnRegister NetworkChangeReceiver
        unregisterReceiver(mNetworkChangeReciever);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
