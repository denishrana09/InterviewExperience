package com.example.denish.interviewexperience;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by denish on 20/08/18.
 */

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view,int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public RecyclerItemClickListener(Context context,final RecyclerView recyclerView,OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
//                return super.onSingleTapUp(e);
                View childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childview!=null && mListener!=null){
                    Log.d(TAG, "onSingleTapUp: listener.onItemClick");
                    mListener.OnItemClick(childview, recyclerView.getChildAdapterPosition(childview));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
//                super.onLongPress(e);
                View childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childview!=null && mListener!=null){
                    Log.d(TAG, "onSingleTapUp: listener.onItemClick");
                    mListener.OnItemLongClick(childview, recyclerView.getChildAdapterPosition(childview));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        //return super.onInterceptTouchEvent(rv, e);
        //************************************************************
        if(mGestureDetector!=null){
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned "+ result);
            return result;
        }
        else {
            Log.d(TAG, "onInterceptTouchEvent: returned false");
            return false;
        }
        //********************Added after "recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));" code & is touch events video******************************
    }
}
