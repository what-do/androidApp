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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import android.widget.SearchView;

import static com.reyesc.whatdo.LoginActivity.IMG;
import static com.reyesc.whatdo.LoginActivity.USER;

public class FragmentProfile extends FragmentExtension implements View.OnClickListener {
    private static final String TAG = "FragmentProfile";
    private User mUser;
    View view;
    private ImageView imageView;
    private Uri imgUri;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private InterestRecyclerViewAdapter recyclerViewAdapter;
    private LinearLayout profileLayout;
    private EditText editText;
    private Button submitButton;
    private TextView username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUser = (User)getArguments().getSerializable(USER);
        imgUri = getArguments().getParcelable(IMG);

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);

        initRecyclerView();


        if (mUser.getUserInterests().isEmpty()){
            populatePossibleInterests();
        }
        username = view.findViewById(R.id.username);
        editText = view.findViewById(R.id.editText);
        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        profileLayout = view.findViewById(R.id.profileLayout);
        setProfile();
        return view;
    }



    private void signOut() {
        mUser.clear();
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        intent.putExtra("signout", true);
        startActivity(intent);
    }

    public void setProfile() {
        TextView displayname = view.findViewById(R.id.displayname);
        displayname.setText(mUser.getDisplayName());
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "users", "", mUser.getUserId(), new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    String username = response.getString("username");
                    Log.i(TAG, response.toString());
                    Log.i(TAG, "getting user");
                    Log.i(TAG, "username");
                    Log.i(TAG, username);
                    mUser.setUserName(username);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (mUser.getUserName() == "") {
            username.setVisibility(View.GONE);
            profileLayout.setVisibility(View.VISIBLE);
        } else {
            username.setVisibility(View.VISIBLE);
            username.setText(mUser.getUserName());
            profileLayout.setVisibility(View.GONE);
            requestHttp.postRequest(view.getContext(), mUser.getUserId(), mUser.getUserEmail(), mUser.getUserName(), mUser.getDisplayName());
            Log.i(TAG, "created new user");
        }
        try {
            imageView = view.findViewById(R.id.img);
            new LoadProfileImage().execute(imgUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initRecyclerView() {
        Log.d(TAG, "init Recycler View");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewAdapter =
                new InterestRecyclerViewAdapter(
                        mUser.getUserInterests(),
                        view.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        searchInit();
    }

    public void initInterests() {
        populatePossibleInterests();

        String[] interests = {"hiking", "amusementparks", "airsoft", "archery", "carousels", "boating", "carousels","amusement parks", "bungee jumping"};
        try {
            updateInterest("removeinterests");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateInterest(String method)
            throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (String i : mUser.sendInterests()){
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
                    for(int i = 0; i < response.length(); i++){
                        mUser.addUserInterest((String)response.get(i));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void populatePossibleInterests() {
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "tags", "", "", new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i = 0; i < response.length(); i++){
                        mUser.addUserInterest(((JSONObject)response.get(i)).getString("name"));
                    }
                    getInterests();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout_button:
                signOut();
                break;
            case R.id.submitButton:
                mUser.setUserName(editText.getText().toString());
                setProfile();
                break;
        }
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

    private void searchInit() {
        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<Interest> newInterests = new ArrayList<>();
                newInterests.addAll(query(newText));
                recyclerViewAdapter.setFilter(newInterests);
                return true;
            }
        });

    }
    public ArrayList<Interest> query(String query){
        final ArrayList<Interest> newInterests = new ArrayList<>();
        for (Interest i : mUser.getUserInterests()){
            if(i.getTag().toLowerCase().startsWith(query.toLowerCase())){
                newInterests.add(i);
            }
        }
        return newInterests;
    }
}
