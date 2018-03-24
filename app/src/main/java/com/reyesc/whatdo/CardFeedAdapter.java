package com.reyesc.whatdo;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardFeedAdapter extends RecyclerView.Adapter<CardFeedAdapter.CardViewHolder> {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<ActivityCard> cardList;
    private int lastVisibleItem, totalItemCount, visibleThreshold = 3, loadCount = 10;
    private boolean loading;

    public CardFeedAdapter(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView){
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCardFeed();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadCardFeed();
            }
        });

        cardList = new ArrayList<ActivityCard>();
        loadMoreCards();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ActivityCard activityCard = cardList.get(position);

        //holder.imageView.setImageDrawable(context.getDrawable(activityCard.getImage()));
        holder.textViewDate.setText(activityCard.getDate());
        holder.textViewTitle.setText(activityCard.getTitle());
        holder.textViewTags.setText(activityCard.getTags());
        holder.textViewDescription.setText(activityCard.getDescription());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewDate, textViewTitle, textViewTags, textViewDescription;

        public CardViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTags = itemView.findViewById(R.id.textViewTags);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }


    private void refreshCardFeed(){
        MainActivity.toasting("Refreshing");
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadCardFeed() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached, do something
            MainActivity.toasting("More cards loaded");
            loading = true;
            loadMoreCards();
        }
    }

    private void loadMoreCards(){
        for(int i = 0; i < loadCount; i++){
            cardList.add(new ActivityCard(i,0,"Date\n31", "Title" + (totalItemCount + i), "Tags", "Description"));
        }
        this.notifyItemInserted(cardList.size());
        loading = false;
    }
}
