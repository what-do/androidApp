package com.reyesc.whatdo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Michael on 4/17/18.
 */

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.FriendsViewHolder> {

    private static final String TAG = "FriendRecyclerViewAdapt";

    private ArrayList<String> mFriends = new ArrayList<>();
    private Context mContext;



    public FriendRecyclerViewAdapter(ArrayList<String> friends, Context context){
        mFriends = friends;
        mContext = context;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends, parent, false);
        FriendsViewHolder holder = new FriendsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.friendName.setText(mFriends.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mFriends.get(position));

                Toast.makeText(mContext, mFriends.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder{

        TextView friendName;

        RelativeLayout parentLayout;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            parentLayout = itemView.findViewById(R.id.friendLayout);
        }
    }
}