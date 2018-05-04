package com.reyesc.whatdo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  FragmentFriends extends FragmentExtension implements View.OnClickListener {

    private static final String TAG = "FragmentFriends";
    View view;
    private Button sendReqButton;
    private EditText friendId;
    private User mUser = User.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        sendReqButton = view.findViewById(R.id.sendReq);
        friendId = view.findViewById(R.id.friendId);
        sendReqButton.setOnClickListener(this);
        getFriends();
        getFriendRequests();
        initFriendRecyclerView();
        return view;
    }

    public void initFriendRecyclerView() {
        Log.d(TAG, "init Friend Recycler View");
        /*if (mUser.getUserFriends().isEmpty()) { //TODO: remove hard code
            mUser.addFriend(new Friend("1","jaime"));
            mUser.addFriend(new Friend("2", "andres"));
            mUser.addFriend(new Friend("3", "michael"));
            mUser.addFriend(new Friend("4","brian"));
            Friend newReq = new Friend("5", "leigh");
            newReq.setReq(true);
            mUser.addFriend(newReq);
            Friend newReq2 = new Friend("6", "test");
            newReq2.setReq(true);
            mUser.addFriend(newReq2);
        }*/
        RecyclerView recyclerView = view.findViewById(R.id.friendRecyclerView);
        FriendRecyclerViewAdapter recyclerViewAdapter =
                new FriendRecyclerViewAdapter(mUser.getUserFriends(), view.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void getFriends() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "users", "", mUser.getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    Log.i(TAG, "getting friend list");
                    JSONObject response = new JSONObject(result);
                    Log.i(TAG, response.toString());
                    JSONArray friendsList = (JSONArray)response.get("friendList");
                    for (int i = 0; i < friendsList.length(); i++) {
                        JSONObject jsonObject = (JSONObject)friendsList.get(i);
                        String friendId = jsonObject.getString("id");
                        String friendUserName = jsonObject.getString("username");
                        mUser.addFriend(new Friend(friendId, friendUserName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFriendRequests() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "friendrequest", "", mUser.getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    Log.i(TAG, "getting friend request list");
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String senderId = jsonObject.getString("_id");
                        String senderName = jsonObject.getString("senderName");
                        Friend newReq = new Friend(senderId, senderName);
                        newReq.setReq(true);
                        mUser.addFriend(newReq);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sendReq:
                sendReq(friendId.getText().toString());
                break;
        }
    }

    public void sendReq(String id) {
        Log.i(TAG, "friend request sent");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(id);
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.postRequest(view.getContext(), mUser.getUserId(), id, 0, null);
    }
}
