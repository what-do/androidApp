package com.reyesc.whatdo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class CardFeedAdapter extends RecyclerView.Adapter<CardViewHolder> implements CardTouchHelper.CardTouchHelperListener {
    private RecyclerView recyclerView;
    private List<ActivityCard> cardList;
    private int visibleThreshold = 3;
    private int loadCount = 20;
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

        ItemTouchHelper.SimpleCallback cardTouchHelperCallback = new CardTouchHelper(0,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(cardTouchHelperCallback).attachToRecyclerView(recyclerView);

        loadMoreCards();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CardViewHolder(view, fragmentToActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bindData(cardList.get(position));
        if (holder.getTextViewTitle().getText().toString().contains("Sponsored")) {
            holder.getCardView().findViewById(R.id.cardViewBack).setBackgroundColor(Color.parseColor("#FFFACD"));
            holder.getCardView().findViewById(R.id.cardViewFront).setBackgroundColor(Color.parseColor("#FFFACD"));
        }
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
    }

    protected void restoreCard(ActivityCard card) {
        int position = 0;
        while (position < cardList.size() && card.getIdx() > cardList.get(position).getIdx()) {
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
            String name = cardList.get(viewHolder.getAdapterPosition()).getName();

            final ActivityCard deletedItem = cardList.get(viewHolder.getAdapterPosition());
            final JSONArray swipedCard = new JSONArray();
            swipedCard.put(deletedItem.getYelp());

            removeCard(deletedItem);

            Snackbar snackbar;
            if (direction == ItemTouchHelper.LEFT) {
                RequestHttp requestHttp = RequestHttp.getRequestHttp();
                requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"removelike", swipedCard);

                snackbar = Snackbar.make(recyclerView.findViewById(R.id.cardFeedDiscover), name + " removed from list!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreCard(deletedItem);
                        RequestHttp requestHttp = RequestHttp.getRequestHttp();
                        requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"addlike", swipedCard);
                    }
                });
                snackbar.getView().setBackgroundColor(Color.parseColor("#C40233"));
            } else {
                RequestHttp requestHttp = RequestHttp.getRequestHttp();
                requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"addlike", swipedCard);

                snackbar = Snackbar.make(recyclerView.findViewById(R.id.cardFeedDiscover), name + " saved for later!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreCard(deletedItem);
                        RequestHttp requestHttp = RequestHttp.getRequestHttp();
                        requestHttp.putStringRequest(recyclerView.getContext(), User.getInstance().getUserId(),"removelike", swipedCard);
                    }
                });
                snackbar.getView().setBackgroundColor(Color.parseColor("#009F6B"));
            }
            snackbar.setActionTextColor(Color.WHITE);
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

    private void loadMoreCards() {
        System.out.println(User.getInstance().getActivityFilter());
        if (User.getInstance().getActivityFilter().equals("I'm by myself!")) {
            RequestHttp requestHttp = RequestHttp.getRequestHttp();
            requestHttp.getRequest(recyclerView.getContext(), "users", "activities", User.getInstance().getUserId(), new RequestHttp.VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONArray response = new JSONArray(result);
                        Log.i(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject activity = response.getJSONObject(i);
                            String name = activity.getString("name");
                            JSONArray tagsJSON = activity.getJSONArray("tags");
                            String tags = null;
                            for (int j = 0; j < tagsJSON.length(); j++) {
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
                            boolean sponsored = false;
                            if (totalLoaded == 0 || totalLoaded % 10 - 1 == 0) {
                                name = "(Sponsored) " + name;
                                sponsored = true;
                            }
                            cardList.add(new ActivityCard(totalLoaded, id, image, "Date\n31", name, tags, description, address, yelp, sponsored));
                            totalLoaded++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading = false;
                    notifyDataSetChanged();
                }
            });
        } else {
            RequestHttp requestHttp = RequestHttp.getRequestHttp();
            requestHttp.getJointActivityRequest(recyclerView.getContext(), User.getInstance().getUserId(), User.getInstance().getActivityFilter(), new RequestHttp.VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONArray response = new JSONArray(result);
                        Log.i(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject activity = response.getJSONObject(i);
                            String name = activity.getString("name");
                            JSONArray tagsJSON = activity.getJSONArray("tags");
                            String tags = null;
                            for (int j = 0; j < tagsJSON.length(); j++) {
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
                            boolean sponsored = false;
                            if (totalLoaded == 0 || totalLoaded % 10 - 1 == 0) {
                                name = "(Sponsored) " + name;
                                sponsored = true;
                            }
                            cardList.add(new ActivityCard(totalLoaded, id, image, "Date\n31", name, tags, description, address, yelp, sponsored));
                            totalLoaded++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading = false;
                    notifyDataSetChanged();
                }
            });
        }
    }
}
