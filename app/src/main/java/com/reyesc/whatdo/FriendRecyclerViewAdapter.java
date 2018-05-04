package com.reyesc.whatdo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

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
        int blue = viewHolder.friendName.getResources().getColor(R.color.blue);
        viewHolder.friendName.setText(mFriends.get(position).getFriendUserName());
        if (mFriends.get(position).isReq()) {
            viewHolder.friendName.setTextColor(blue);
            viewHolder.removeFriend.setVisibility(View.GONE);
            viewHolder.acceptReq.setVisibility(View.VISIBLE);
        }

        viewHolder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray removedFriend = new JSONArray();
                removedFriend.put(mFriends.get(position));

                //TODO: test this
                //viewHolder.friendName.setVisibility(View.GONE);
                //viewHolder.removeFriend.setVisibility(View.GONE);

                //RequestHttp requestHttp = RequestHttp.getRequestHttp();
                //TODO: need endpoint for removing friends
                //requestHttp.putStringRequest(mContext, mUser.getUserId(), "removefriends", removedFriend);
            }
        });

        viewHolder.acceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: what does this do
                Toast.makeText(mContext, mFriends.get(position).getFriendUserName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return mFriends.size();
    }
}