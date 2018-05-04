package com.reyesc.whatdo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private static final String TAG = "FriendRecyclerViewAdap";
    private ArrayList<Friend> mFriends = new ArrayList<>();
    private Context mContext;
    private User mUser;

    public FriendRecyclerViewAdapter(ArrayList<Friend> friends, Context context) {
        mContext = context;
        mFriends = friends;
        mUser = User.getInstance();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendViewHolder viewHolder, final int position) {
        Collections.sort(mFriends);
        int blue = viewHolder.friendName.getResources().getColor(R.color.blue);
        viewHolder.friendName.setText(mFriends.get(position).getFriendUserName());
        viewHolder.removeFriend.setVisibility(View.VISIBLE);
        if (mFriends.get(position).isReq()) {
            viewHolder.friendName.setTextColor(blue);
            viewHolder.acceptReq.setVisibility(View.VISIBLE);
        }

        viewHolder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friend removedFriend = mFriends.get(position);
                RequestHttp requestHttp = RequestHttp.getRequestHttp();

                if (removedFriend.isReq()) {
                    //decline friend request
                    Log.i(TAG, "declining request");
                    requestHttp.postRequest(mContext, mUser.getUserId(), removedFriend.getFriendId(), 1, false);
                    mUser.removeFriend(removedFriend.getFriendId());
                    mFriends = mUser.getUserFriends();
                    mFriends.remove(removedFriend);

                } else {
                    //remove friend
                    Log.i(TAG, "removing friend");
                    requestHttp.postRequest(mContext, mUser.getUserId(), removedFriend.getFriendUserName(), 2, null);
                    mUser.removeFriend(removedFriend.getFriendId());
                    mFriends = mUser.getUserFriends();
                    mFriends.remove(removedFriend);
                }
            }
        });

        viewHolder.acceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accept friend request
                Friend newFriend = mFriends.get(position);
                mUser.addFriend(newFriend);
                RequestHttp requestHttp = RequestHttp.getRequestHttp();
                requestHttp.postRequest(mContext, mUser.getUserId(), newFriend.getFriendId(), 1, true);
            }
        });

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mFriends.get(position).getFriendUserName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return mFriends.size();
    }
}