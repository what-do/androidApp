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
import org.json.JSONObject;

import java.util.ArrayList;

public class  FragmentFriends extends FragmentExtension {

    private static final String TAG = "FragmentFriends";
    View view;
    ArrayList friends = new ArrayList<String>();
    private User mUser = User.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        getFriends();
        initFriendRecyclerView();
        return view;
    }

    public void initFriendRecyclerView() {
        Log.d(TAG, "init Friend Recycler View");
        if (mUser.getUserFriends().isEmpty()) { //TODO: remove hard code
            mUser.addFriend("jaime");
            mUser.addFriend("andres");
            mUser.addFriend("michael");
            mUser.addFriend("brian");
            mUser.addFriend("leigh");
            mUser.addFriend("test");
            mUser.addFriend("test1");
            mUser.addFriend("test2");
            mUser.addFriend("test3");
            mUser.addFriend("test4");
            mUser.addFriend("test5");
            mUser.addFriend("test6");

        }
        RecyclerView recyclerView = view.findViewById(R.id.friendRecyclerView);
        FriendRecyclerViewAdapter recyclerViewAdapter =
                new FriendRecyclerViewAdapter(
                        mUser.getUserFriends(),
                        view.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }


    private void getFriends() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "users", "friends", mUser.getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        mUser.addFriend(new JSONObject((String)response.get(i)).getString("username"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addFriend(String friend) {
        mUser.addFriend(friend);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(friend);
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.putStringRequest(view.getContext(), mUser.getUserId(), "addFriends", jsonArray);
    }

    public void removeFriend(String friend) {
        mUser.removeFriend(friend);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(friend);
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.putStringRequest(view.getContext(), mUser.getUserId(), "removeFriends", jsonArray);
    }
}
