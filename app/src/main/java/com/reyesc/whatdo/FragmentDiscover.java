package com.reyesc.whatdo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class FragmentDiscover extends FragmentExtension {
    private View view;
    private ArrayList<ActivityCard> cardList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_discover, container, false);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            RecyclerView recyclerView = view.findViewById(R.id.cardFeedDiscover);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            cardList = new ArrayList<>();
            CardFeedAdapter cardFeed = new CardFeedAdapter(recyclerView, cardList, fragmentToActivityListener);
            recyclerView.setAdapter(cardFeed);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            this.fragmentToActivityListener = (FragmentToActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentToActivityListener");
        }
    }

    public void addToFeed(ActivityCard card) {
        ((CardFeedAdapter) ((RecyclerView) view.findViewById(R.id.cardFeedDiscover)).getAdapter()).restoreCard(card);
    }

    public void removeFromFeed(ActivityCard card) {
        ((CardFeedAdapter) ((RecyclerView) view.findViewById(R.id.cardFeedDiscover)).getAdapter()).removeCard(card);
    }
}
