package com.reyesc.whatdo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ActivityFriendsCardAdapter extends RecyclerView.Adapter<ActivityFriendsCardViewHolder> {
    private RecyclerView recyclerView;
    private List<String> friendsList;
    private int totalLoaded;
    private boolean loading;

    public ActivityFriendsCardAdapter(RecyclerView recyclerView, ArrayList<String> friendsList){
        this.recyclerView = recyclerView;
        this.friendsList = friendsList;
        totalLoaded = 0;

        loadFriends();
    }

    @NonNull
    @Override
    public ActivityFriendsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ActivityFriendsCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityFriendsCardViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bindData(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.card_activity_friend;
    }

    private void loadFriends(){
        for(int i = 0; i < 10; i++){
            friendsList.add(String.valueOf(i));
            totalLoaded++;
            notifyItemInserted(friendsList.size());
        }
        loading = false;
    }
}
