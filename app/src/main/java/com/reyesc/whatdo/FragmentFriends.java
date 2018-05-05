package com.reyesc.whatdo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class  FragmentFriends extends FragmentExtension implements View.OnClickListener {

    private static final String TAG = "FragmentFriends";
    View view;
    private Button removeFriend1;
    private Button removeFriend2;
    private Button removeFriend3;
    private Button removeFriend4;
    private Button removeFriend5;
    private TextView friendName1;
    private TextView friendName2;
    private TextView friendName3;
    private TextView friendName4;
    private TextView friendName5;
    private Button addFriend1;
    private Button addFriend2;
    private Button addFriend3;
    private Button addFriend4;
    private Button addFriend5;
    private TextView friendReq1;
    private TextView friendReq2;
    private TextView friendReq3;
    private TextView friendReq4;
    private TextView friendReq5;
    private Button sendReq;
    private EditText friendId;
    private User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);

        TabHost host = view.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Friends");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Friend Requests");
        host.addTab(spec);

        removeFriend1 = view.findViewById(R.id.removeFriend1);
        removeFriend2 = view.findViewById(R.id.removeFriend2);
        removeFriend3 = view.findViewById(R.id.removeFriend3);
        removeFriend1.setOnClickListener(this);
        removeFriend2.setOnClickListener(this);
        removeFriend3.setOnClickListener(this);

        friendName1 = view.findViewById(R.id.friendName1);
        friendName2 = view.findViewById(R.id.friendName2);
        friendName3 = view.findViewById(R.id.friendName3);

        addFriend1 = view.findViewById(R.id.addFriend1);
        addFriend2 = view.findViewById(R.id.addFriend2);
        addFriend3 = view.findViewById(R.id.addFriend3);
        addFriend1.setOnClickListener(this);
        addFriend2.setOnClickListener(this);
        addFriend3.setOnClickListener(this);

        friendReq1 = view.findViewById(R.id.friendReq1);
        friendReq2 = view.findViewById(R.id.friendReq2);
        friendReq3 = view.findViewById(R.id.friendReq3);

        sendReq = view.findViewById(R.id.sendReq);
        sendReq.setOnClickListener(this);
        friendId = view.findViewById(R.id.friendId);
        mUser = User.getInstance();

        getFriends();
        try {
            if (!mUser.getUserFriends().isEmpty()) {
                friendName1.setText(mUser.getUserFriends().get(0).getFriendUserName());
                friendName2.setText(mUser.getUserFriends().get(1).getFriendUserName());
                friendName3.setText(mUser.getUserFriends().get(2).getFriendUserName());
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        getFriendRequests();

        try {
            if (!mUser.getUserFriendReqs().isEmpty()) {
                friendReq1.setText(mUser.getUserFriendReqs().get(0).getFriendUserName());
                friendReq2.setText(mUser.getUserFriendReqs().get(1).getFriendUserName());
                friendReq3.setText(mUser.getUserFriendReqs().get(2).getFriendUserName());
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return view;
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
                    for (int i = 0; i < response.length(); i++) {
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

                    Log.i(TAG, mUser.getUserId());
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String senderId = jsonObject.getString("_id");
                        String senderName = jsonObject.getString("senderName");
                        Friend newReq = new Friend(senderId, senderName);
                        mUser.addFriendReq(newReq);
                        Log.i(TAG, senderName);
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
                Log.i(TAG, "in send req on click:");
                sendReq(friendId.getText().toString());
                Log.i(TAG, friendId.getText().toString());
                break;
            case R.id.removeFriend1:
                removeFriend(friendName1.getText().toString());
                break;
            case R.id.removeFriend2:
                removeFriend(friendName2.getText().toString());
                break;
            case R.id.removeFriend3:
                removeFriend(friendName3.getText().toString());
                break;
            case R.id.addFriend1:
                addFriend(friendReq1.getText().toString());
                break;
            case R.id.addFriend2:
                addFriend(friendReq2.getText().toString());
                break;
            case R.id.addFriend3:
                addFriend(friendReq3.getText().toString());
                break;
        }
    }

    private void sendReq(String id) {
        Log.i(TAG, "friend request sent");
        Log.i(TAG, id);
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.postRequest(view.getContext(), mUser.getUserId(), id, 0, null);
        update();
    }

    private void removeFriend(String friend) {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        Log.i(TAG, "removing friend");

        requestHttp.postRequest(view.getContext(), mUser.getUserId(), friend, 2, null);
        mUser.removeFriend(friend);
        System.out.println(mUser.getUserFriends());
        update();
    }

    private void addFriend(String userName) {
        String id = mUser.findFriendReqId(userName);
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.postRequest(view.getContext(), mUser.getUserId(), id, 1, true);
        mUser.addFriend(new Friend(id, userName));
        mUser.removeFriendReq(id);
        System.out.println(mUser.getUserFriendReqs());
        update();

    }

    private void update() {

        getFriends();
        try {
            if (!mUser.getUserFriends().isEmpty()) {
                friendName1.setText(mUser.getUserFriends().get(0).getFriendUserName());
                friendName2.setText(mUser.getUserFriends().get(1).getFriendUserName());
                friendName3.setText(mUser.getUserFriends().get(2).getFriendUserName());
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        getFriendRequests();

        try {
            if (!mUser.getUserFriendReqs().isEmpty()) {
                friendReq1.setText(mUser.getUserFriendReqs().get(0).getFriendUserName());
                friendReq2.setText(mUser.getUserFriendReqs().get(1).getFriendUserName());
                friendReq3.setText(mUser.getUserFriendReqs().get(2).getFriendUserName());
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
