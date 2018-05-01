package com.reyesc.whatdo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class  FragmentFriends extends FragmentExtension {

    private static final String TAG = "FragmentFriends";
    View view;
    ArrayList friends = new ArrayList<String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        initFriendRecyclerView();
        return view;
    }

    public void initFriendRecyclerView(){
        Log.d(TAG, "init Friend Recycler View");
        friends.add("josh");
        friends.add("jim");
        friends.add("kevin");
        friends.add("andresito");
        friends.add("brianito");
        RecyclerView recyclerView = view.findViewById(R.id.friendRecyclerView);
        FriendRecyclerViewAdapter fRecyclerViewAdapter = new FriendRecyclerViewAdapter(friends, view.getContext());
        recyclerView.setAdapter(fRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void initRequestRecyclerView(){}

    public void populateFriendsList(){
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "users", "friends", "12345", new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        friends.add(i, (String) response.get(i));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addFriends(ArrayList<String> friend){
        JSONArray jsonArray = new JSONArray();
        for(String i : friend){
            jsonArray.put(i);
        }
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.putStringRequest(view.getContext(), "12345", "addFriends", jsonArray);
    }
}
