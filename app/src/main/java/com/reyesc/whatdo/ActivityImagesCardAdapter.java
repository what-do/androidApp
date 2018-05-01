package com.reyesc.whatdo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ActivityImagesCardAdapter extends RecyclerView.Adapter<ActivityImagesCardViewHolder> {
    private RecyclerView recyclerView;
    private List<String> imagesList;
    private int totalLoaded;
    private boolean loading;

    public ActivityImagesCardAdapter(RecyclerView recyclerView, ArrayList<String> imagesList){
        this.recyclerView = recyclerView;
        this.imagesList = imagesList;
        totalLoaded = 0;

        loadImages();
    }

    @NonNull
    @Override
    public ActivityImagesCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ActivityImagesCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityImagesCardViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bindData(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.card_activity_image;
    }

    private void loadImages(){
        for(int i = 0; i < 10; i++){
            imagesList.add(String.valueOf(i));
            totalLoaded++;
            notifyItemInserted(imagesList.size());
        }
        loading = false;
    }
}
