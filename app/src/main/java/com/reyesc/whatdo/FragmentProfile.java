package com.reyesc.whatdo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.reyesc.whatdo.LoginActivity.IMG;
import static com.reyesc.whatdo.LoginActivity.USER;

public class FragmentProfile extends FragmentExtension implements View.OnClickListener {
    private static final String TAG = "FragmentProfile";
    private User mUser;
    View view;
    private ImageView imageView;
    private Uri imgUri;

    ArrayList<String> interests =  new ArrayList<>();
    ArrayList<String> possibleInterests =  new ArrayList<>();
    ArrayList<Boolean> checkBoxes = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUser = (User)getArguments().getSerializable(USER);
        imgUri = getArguments().getParcelable(IMG);

        Button logoutButton = view.findViewById(R.id.logout_button);
        Button updateButton = view.findViewById(R.id.update_button);

        logoutButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);

        initRecyclerView();
        initInterests();
        setProfile();

        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout_button:
                signOut();
                break;
            case R.id.update_button:
                getInterests();
                break;
        }
    }

    private void signOut() {
        mUser.clear();
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        intent.putExtra("signout", true);
        startActivity(intent);
    }

    //TODO: if getUser == null then create User
    public void createUser() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.postRequest(view.getContext(), mUser.getUserId(), mUser.getEmail(), mUser.getUserName());
    }

    //TODO: what does get req return if no user
    /*public void getUser() {
    }*/

    public void setProfile() {
        imageView = view.findViewById(R.id.img);
        new LoadProfileImage().execute(imgUri.toString());
        TextView name = view.findViewById(R.id.name);
        name.setText(mUser.getUserName());
    }

    public void initRecyclerView() {
        Log.d(TAG, "init Recycler View");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(possibleInterests, checkBoxes, view.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void initInterests() {
        populatePossibleInterests();

        String[] interests = {"hiking", "amusementparks", "airsoft"};
        try {
            updateInterest(interests, "addinterests");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateInterest(String[] interests, String method)
            throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(String i : interests) {
            jsonArray.put(i);
        }
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.putStringRequest(view.getContext(), mUser.getUserId(), method, jsonArray);
    }

    public void getInterests() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "users", "interests", mUser.getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    interests.clear();
                    for(int i = 0; i < response.length(); i++){
                        interests.add(i, (String)response.get(i));
                    }
                    System.out.println("final interests: " + interests.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void populatePossibleInterests(){
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "tags", "", "", new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for(int i = 0; i < response.length(); i++){
                        if(possibleInterests.size() > i) {
                            possibleInterests.set(i, ((JSONObject) response.get(i)).getString("alias"));
                        }
                        else {
                            possibleInterests.add(((JSONObject) response.get(i)).getString("alias"));
                        }
                        if(interests.contains(possibleInterests.get(i))) {
                            if (checkBoxes.size() > i) {
                                checkBoxes.set(i, true);
                            }
                            else {
                                checkBoxes.add(i, true);
                            }
                        }
                        else{
                            if (checkBoxes.size() > i) {
                                checkBoxes.set(i, false);
                            }
                            else {
                                checkBoxes.add(i, false);
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            String imgUrl = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new URL(imgUrl).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
