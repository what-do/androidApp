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

public class InterestRecyclerViewAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private static final String TAG = "InterestRecyclerViewA";
    private ArrayList<Interest> mInterests = new ArrayList<>();
    private Context mContext;
    private User mUser;

    public InterestRecyclerViewAdapter(ArrayList<Interest> interests, Context context) {
        mInterests = interests;
        mContext = context;
        mUser = User.getInstance();
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests, parent, false);
        InterestViewHolder viewHolder = new InterestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InterestViewHolder viewHolder, final int position) {

        viewHolder.checkBox.setChecked(mInterests.get(position).isInterested());
        viewHolder.interestTag.setText(mInterests.get(position).getTag());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray changedInterest = new JSONArray();
                if (viewHolder.checkBox.isChecked()){
                    mInterests.get(position).select();
                    String addedInterest = mInterests.get(position).getTag();
                    System.out.println("New interest: " + mInterests.get(position).getTag());
                    mUser.addUserInterest(mInterests.get(position).getTag());
                    RequestHttp requestHttp = RequestHttp.getRequestHttp();
                    changedInterest.put(addedInterest.toLowerCase());
                    requestHttp.putStringRequest(mContext, mUser.getUserId(), "addinterests", changedInterest);
                }
                else{
                    String removedInterest = mInterests.get(position).getTag();
                    mInterests.get(position).deselect();
                    System.out.println("Old interest: " + removedInterest);
                    mUser.removeUserInterest(removedInterest);
                    RequestHttp requestHttp = RequestHttp.getRequestHttp();
                    changedInterest.put(removedInterest.toLowerCase());
                    requestHttp.putStringRequest(mContext, mUser.getUserId(), "removeinterests", changedInterest);

                }
            }
        });

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mInterests.get(position));
                Toast.makeText(mContext, mInterests.get(position).getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFilter(ArrayList<Interest> filter){
        mInterests = new ArrayList<>();
        mInterests.addAll(filter);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mInterests.size();
    }
}
