package com.reyesc.whatdo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class CardCollectionAdapter extends RecyclerView.Adapter<CardViewHolder> implements CardTouchHelper.CardTouchHelperListener {
    private RecyclerView recyclerView;
    private List<ActivityCard> cardList;
    private int visibleThreshold = 3;
    private int loadCount = 20;
    private int totalLoaded;
    private boolean loading;
    private FragmentExtension.FragmentToActivityListener fragmentToActivityListener;

    public CardCollectionAdapter(RecyclerView recyclerView, ArrayList<ActivityCard> cardList, FragmentExtension.FragmentToActivityListener fragmentToActivityListener){
        this.recyclerView = recyclerView;
        this.cardList = cardList;
        this.fragmentToActivityListener = fragmentToActivityListener;
        totalLoaded = 0;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadCardCollection();
            }
        });

        ItemTouchHelper.SimpleCallback cardTouchHelperCallback = new CardTouchHelper(0,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(cardTouchHelperCallback).attachToRecyclerView(recyclerView);

        loadMoreCards();
        checkEmpty();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_activity, parent, false);
        return new CardViewHolder(view, fragmentToActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        ActivityCard activityCard = cardList.get(position);
        activityCard.setSaved(true);
        holder.bindData(activityCard);
        holder.getCardView().findViewById(R.id.cardViewBack).setBackgroundColor(Color.parseColor("#ffffff"));
        holder.getCardView().findViewById(R.id.cardViewFront).setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.card_activity;
    }

    protected void removeCard(ActivityCard card) {
        int position = cardList.indexOf(card);
        cardList.remove(position);
        notifyDataSetChanged();
        checkEmpty();
    }

    protected void restoreCard(ActivityCard card) {
        int position = 0;
        while (position < cardList.size() && card.getIdx() > cardList.get(position).getIdx()) {
            position++;
        }

        if (position < cardList.size()) {
            cardList.add(position, card);
            this.notifyItemInserted(position);
        } else {
            cardList.add(card);
            this.notifyItemInserted(cardList.size() - 1);
        }
        checkEmpty();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CardViewHolder) {
            if (direction == ItemTouchHelper.LEFT) {
                String name = cardList.get(viewHolder.getAdapterPosition()).getName();

                final ActivityCard deletedItem = cardList.get(viewHolder.getAdapterPosition());
                deletedItem.setSaved(false);
                final JSONArray swipedCard = new JSONArray();
                swipedCard.put(deletedItem.getYelp());
                removeCard(deletedItem);

                RequestHttp requestHttp = RequestHttp.getRequestHttp();
                requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"removelike", swipedCard);

                Snackbar snackbar = Snackbar.make(recyclerView.findViewById(R.id.cardCollectionSaved), name + " removed from list!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreCard(deletedItem);
                        RequestHttp requestHttp = RequestHttp.getRequestHttp();
                        requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"addlike", swipedCard);
                    }
                });
                snackbar.getView().setBackgroundColor(Color.parseColor("#C40233"));
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        }
    }

    private void loadCardCollection() {
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

    public void loadMoreCards(){
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(recyclerView.getContext(), "users", "likes", User.getInstance().getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for(int i = 0; i < response.length(); i++){
                        JSONObject activity = response.getJSONObject(i);
                        String name = activity.getString("name");
                        JSONArray tagsJSON = activity.getJSONArray("tags");
                        String tags = null;
                        for(int j = 0; j < tagsJSON.length(); j++) {
                            if (tags == null) {
                                tags = "#" + tagsJSON.getJSONObject(j).getString("title");
                            } else {
                                tags += " #" + tagsJSON.getJSONObject(j).getString("title");
                            }
                        }
                        JSONArray addressJSON = activity.getJSONArray("address").getJSONObject(0).getJSONArray("display_address");
                        String address = addressJSON.getString(0) + "\n" + addressJSON.getString(1);
                        String id = activity.getString("_id");
                        String image = activity.getString("image");
                        String yelp = activity.getString("yelp");
                        String description = activity.getString("description");
                        cardList.add(new ActivityCard(totalLoaded, id, image,"Date\n31", name, tags, description, address, yelp, false));
                        totalLoaded++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                loading = false;
                notifyDataSetChanged();
            }
        });
        checkEmpty();
    }

    private void checkEmpty(){
        if (cardList.isEmpty()) {
            ((View)recyclerView.getParent()).findViewById(R.id.noSaved).setVisibility(View.VISIBLE);
        } else {
            ((View)recyclerView.getParent()).findViewById(R.id.noSaved).setVisibility(View.INVISIBLE);
        }
    }
}
