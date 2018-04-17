package com.reyesc.whatdo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CardFeedAdapter extends RecyclerView.Adapter<CardViewHolder> implements CardTouchHelper.CardTouchHelperListener {
    private RecyclerView recyclerView;
    private List<ActivityCard> cardList;
    private int visibleThreshold = 3;
    private int loadCount = 10;
    private int totalLoaded;
    private boolean loading;
    private FragmentExtension.FragmentToActivityListener fragmentToActivityListener;

    public CardFeedAdapter(RecyclerView recyclerView, ArrayList<ActivityCard> cardList, FragmentExtension.FragmentToActivityListener fragmentToActivityListener){
        this.recyclerView = recyclerView;
        this.cardList = cardList;
        this.fragmentToActivityListener = fragmentToActivityListener;
        totalLoaded = 0;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadCardFeed();
            }
        });

        ItemTouchHelper.SimpleCallback cardTouchHelperCallback = new CardTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(cardTouchHelperCallback).attachToRecyclerView(recyclerView);

        loadMoreCards();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bindData(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.activity_card;
    }

    protected void removeCard(ActivityCard card) {
        int position = cardList.indexOf(card);
        cardList.remove(position);
        notifyDataSetChanged();
    }

    protected void restoreCard(ActivityCard card) {
        int position = 0;
        while (position < cardList.size() && card.getId() > cardList.get(position).getId()) {
            position++;
        }

        if (position < cardList.size()) {
            cardList.add(position, card);
            notifyItemInserted(position);
        } else {
            cardList.add(card);
            notifyItemInserted(cardList.size() - 1);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CardViewHolder) {
            String name = cardList.get(viewHolder.getAdapterPosition()).getTitle();

            final ActivityCard deletedItem = cardList.get(viewHolder.getAdapterPosition());

            Snackbar snackbar;
            if (direction == ItemTouchHelper.LEFT) {
                removeCard(deletedItem);
                snackbar = Snackbar.make(recyclerView.findViewById(R.id.cardFeedDiscover), name + " removed from list!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreCard(deletedItem);
                    }
                });
            } else {
                fragmentToActivityListener.fromFeedToCollection(deletedItem);
                snackbar = Snackbar.make(recyclerView.findViewById(R.id.cardFeedDiscover), name + " saved for later!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentToActivityListener.fromCollectionToFeed(deletedItem);
                    }
                });
            }
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void loadCardFeed() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached, do something
            fragmentToActivityListener.toasting("More cards loaded");
            loading = true;
            loadMoreCards();
        }
    }

    private void loadMoreCards(){
        for(int i = 0; i < loadCount; i++){
            cardList.add(new ActivityCard((totalLoaded),0,"Date\n31", "Title" + (totalLoaded), "Tags", "Description"));
            totalLoaded++;
            notifyItemInserted(cardList.size());
        }
        loading = false;
    }
}
