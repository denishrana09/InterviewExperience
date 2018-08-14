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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ManActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mUsersDBRef;

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private String mUsername,isExecuted;

    private NetworkChangeReciever mNetworkChangeReciever;

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
            Log.d(TAG, "onCreate: before Intent of OnBoardingActivity");
            Intent i1 = new Intent(getApplicationContext(),OnBoardingActivity.class);
            startActivity(i1);
            Log.d(TAG, "onCreate: after Intent of OnBoardingActivity");
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUsersDBRef = mFirebaseDB.getReference().child("users");

        //Network Connection
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkChangeReciever = new NetworkChangeReciever();
        registerReceiver(mNetworkChangeReciever, intentFilter);

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
                        onSignedInInitialized(user.getDisplayName(),user.getEmail());

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", user.getDisplayName());
                        editor.putString("email",user.getEmail());
                        editor.apply();

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

    private void onSignedInInitialized(String displayName,String email) {
        mUsername = displayName;
        //bug : if user already exist
        //bug : pushing data also on signing out
        User user = new User(mUsername,"avatar_person.png",
                "Hello!!",email);
        String userId = mUsersDBRef.push().getKey();
        mUsersDBRef.child(userId).setValue(user);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Database", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userId", userId.toString());
        editor.apply();
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
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
