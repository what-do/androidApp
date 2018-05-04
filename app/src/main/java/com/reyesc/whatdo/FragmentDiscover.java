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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FragmentDiscover extends FragmentExtension {
    private View view;
    private ArrayList<ActivityCard> cardList;
    private String selected = null;


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

            Spinner sp = view.findViewById(R.id.filter);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, User.getInstance().getUserFriendsStringArray());
            sp.setAdapter(spinnerAdapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected = User.getInstance().getUserFriendsStringArray().get(position);
                    System.out.println(selected);
                    User.getInstance().setActivityFilter(selected);
                    System.out.println(User.getInstance().getActivityFilter());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
