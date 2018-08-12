package com.example.denish.interviewexperience;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by denish on 8/8/18.
 */

public class NetworkChangeReciever extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;
    private static final String TAG = "NetworkChangeReciever";

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
//        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
//        if (isConnected) {
//            Log.i("NET", "Connected " + isConnected);
//            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Log.i("NET", "Not Connected " + isConnected);
//            Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                Log.d(TAG, "isNetworkAvailable: Info Not Null");
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.d(TAG, "Now you are connected to Internet!");
                            Toast.makeText(context, "Internet availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
                            isConnected = true;
                            // do your processing here ---
                            // if you need to post any data to the server or get
                            // status
                            // update from the server
                        }
                        return true;
                    }
                }
            }
        }
        Log.d(TAG, "isNetworkAvailable: connected to Internet");
        Toast.makeText(context, "Internet NOT availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
        isConnected = false;
        return false;
    }

//    @Override
//    public void onReceive(final Context context, final Intent intent) {
//
//        String status = NetworkUtil.getConnectivityStatusString(context);
//
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
//    }
}
