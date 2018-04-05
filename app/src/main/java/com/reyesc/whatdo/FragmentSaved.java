package com.reyesc.whatdo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentSaved extends FragmentExtension {
    private View view;
    private ArrayList<ActivityCard> cardList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_saved, container, false);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            RecyclerView recyclerView = view.findViewById(R.id.cardCollectionSaved);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            cardList = new ArrayList<>();
            CardCollectionAdapter cardCollection = new CardCollectionAdapter(swipeRefreshLayout, recyclerView, cardList, fragmentToActivityListener);
            recyclerView.setAdapter(cardCollection);
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

    public void addToCollection(ActivityCard card) {
        ((CardCollectionAdapter) ((RecyclerView) view.findViewById(R.id.cardCollectionSaved)).getAdapter()).restoreCard(card);
    }

    public void removeFromCollection(ActivityCard card) {
        ((CardCollectionAdapter) ((RecyclerView) view.findViewById(R.id.cardCollectionSaved)).getAdapter()).removeCard(card);
    }
}
