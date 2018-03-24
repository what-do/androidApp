package com.reyesc.whatdo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    private List<ActivityCard> cardList;
    private RecyclerView cardFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        cardFeed = view.findViewById(R.id.cardFeedDiscover);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardFeed.setLayoutManager(layoutManager);

        cardList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            cardList.add(
                    // ActivityCard(int id, int image, String date, String title, String tags, String description)
                    new ActivityCard(i,0,"Date\n31", "Title", "Tags", "Description"));
        }

        CardFeedAdapter adapter = new CardFeedAdapter(cardList);
        cardFeed.setAdapter(adapter);

        return view;
    }
}
