package com.example.denish.interviewexperience;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denish.interviewexperience.model.LikedPost;
import com.example.denish.interviewexperience.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denish on 16/8/18.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<Post> mPostList;
    private String theusername,userid;
    private boolean isLiked = false;
    private int oldLikes;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPostDBRef;
    private DatabaseReference mLikedPostDBRef;
    private ChildEventListener mPostChildEventListener;

    public RecyclerViewAdapter(Context mContext, List<Post> mPostList, String username) {
        this.mContext = mContext;
        this.mPostList = mPostList; // initializing by sending (new ArrayList<Photo>()) in MainActivity
        this.theusername = username;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ViewHolder";
        TextView company=null,position=null,username=null,postdate=null
                ,totallikes=null,totalcomments=null,like=null,comment=null,description=null;
        ImageButton btn_like=null,btn_comment=null,btn_save=null;

        public ViewHolder(View itemView) {
            super(itemView);
            this.company = itemView.findViewById(R.id.tv_company_listitem);
            this.position = itemView.findViewById(R.id.tv_position_listitem);
            this.username = itemView.findViewById(R.id.tv_username_listitem);
            this.totallikes = itemView.findViewById(R.id.tv_total_likes);
            this.totalcomments = itemView.findViewById(R.id.tv_total_comment);
            this.like = itemView.findViewById(R.id.tv_like_listeitem);
            this.comment = itemView.findViewById(R.id.tv_comment_listitem);
            this.postdate = itemView.findViewById(R.id.tv_date_listitem);
            this.description = itemView.findViewById(R.id.tv_description_listitem);

            this.btn_like = itemView.findViewById(R.id.btn_like_listitem);
            this.btn_comment = itemView.findViewById(R.id.btn_comment_listitem);
            this.btn_save = itemView.findViewById(R.id.btn_save_listitem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDBRef = mFirebaseDatabase.getReference().child("posts");
        mLikedPostDBRef = mFirebaseDatabase.getReference().child("likedpost");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //called by layout manager when it wants new data in existing row
        final Post postItem = mPostList.get(position);
        final int pos = position;

        SharedPreferences pref = mContext.getSharedPreferences("Database", 0);
        userid = pref.getString("userId","");

        holder.company.setText(postItem.getCompany());
        holder.position.setText(postItem.getPosition());
        holder.username.setText(theusername);
        holder.like.setText(" like");
        holder.comment.setText(" comment");
        holder.postdate.setText(postItem.getDate());

        String str = postItem.getDescription();
        String result = str.replaceAll("(?m)(^\\s+|[\\t\\f ](?=[\\t\\f ])|[\\t\\f ]$|\\s+\\z)", "");
        holder.description.setText(result);
//        holder.description.setEnabled(false);
//        holder.description.setInputType(InputType.TYPE_NULL);
        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLiked){
                    isLiked = true;
                    mPostDBRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Post post = dataSnapshot.getValue(Post.class);
                            String postId = dataSnapshot.getKey();
                            if(postItem.getUserid().equals(post.getUserid()) &&
                                    postItem.getDescription().equals(post.getDescription())){
                                mPostDBRef.child(postId).child("likes").setValue(post.getLikes()+1);
                                notifyDataSetChanged();

                                LikedPost likedPost = new LikedPost(userid,postId);
                                mLikedPostDBRef.push().setValue(likedPost);

//                                holder.btn_like.setColorFilter(mContext.getResources().getColor(R.color.colorAccent));
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
                    });
                }else {
                    isLiked = false;
                    mPostDBRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Post post = dataSnapshot.getValue(Post.class);
                            final String postId = dataSnapshot.getKey();
                            if(postItem.getUserid().equals(post.getUserid()) &&
                                    postItem.getDescription().equals(post.getDescription())){
                                oldLikes = post.getLikes();
                                oldLikes -= 1;
                                mPostDBRef.child(postId).child("likes").setValue(post.getLikes()-1);
                                notifyDataSetChanged();


                                mLikedPostDBRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        LikedPost likedPost = dataSnapshot.getValue(LikedPost.class);
                                        String likedPostId = dataSnapshot.getKey();
                                        if(likedPost.getUserid().equals(userid) && likedPost.getPostid().equals(postId)){
                                            mLikedPostDBRef.child(likedPostId).removeValue();
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
                                });

//                                holder.btn_like.setColorFilter(mContext.getResources().getColor(R.color.black));
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
                    });
                }


            }
        });
        holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Commented", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        holder.totallikes.setText(postItem.getLikes()+ " likes");
        holder.totalcomments.setText(postItem.getComments() + " comments");
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: called");
        return ((mPostList!=null) && (mPostList.size()!=0) ? mPostList.size() : 0);
    }

    public Post getPost(int position){
        return ((mPostList!=null) && (mPostList.size()!=0) ? mPostList.get(position) : null);
    }



}














