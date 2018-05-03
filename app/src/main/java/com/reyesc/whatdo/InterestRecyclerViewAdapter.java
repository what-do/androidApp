package com.reyesc.whatdo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class InterestRecyclerViewAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private static final String TAG = "InterestRecyclerViewA";
    private ArrayList<Interest> mInterests = new ArrayList<>();
    private Context mContext;

    public InterestRecyclerViewAdapter(ArrayList<Interest> interests, Context context) {
        mInterests = interests;
        mContext = context;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests, parent, false);
        InterestViewHolder viewHolder = new InterestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.checkBox.setChecked(mInterests.get(position).isInterested());
        viewHolder.interestTag.setText(mInterests.get(position).getTag());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mInterests.get(position));
                Toast.makeText(mContext, mInterests.get(position).getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInterests.size();
    }
}
