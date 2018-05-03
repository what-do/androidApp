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

    //ArrayList<Interest> interests =  new ArrayList<>();
    ArrayList<Interest> possibleInterests =  new ArrayList<>();
    //ArrayList<Boolean> checkBoxes = new ArrayList<>();


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
        setProfile();
        createUser();

        if (mUser.getUserInterests().isEmpty()){
            populatePossibleInterests();
        }
        getInterests();

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
        requestHttp.postRequest(view.getContext(), mUser.getUserId(), mUser.getUserEmail(), mUser.getUserName());
    }

    //TODO: what does get req return if no user
    /*public void getUser() {
    }*/

    public void setProfile() {
        TextView name = view.findViewById(R.id.name);
        name.setText(mUser.getUserName());
        try {
            imageView = view.findViewById(R.id.img);
            new LoadProfileImage().execute(imgUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initRecyclerView() {
        Log.d(TAG, "init Recycler View");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        InterestRecyclerViewAdapter recyclerViewAdapter =
                new InterestRecyclerViewAdapter(
                        mUser.getUserInterests(),
                        view.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void initInterests() {
        populatePossibleInterests();

        String[] interests = {"hiking", "amusementparks", "airsoft"};
        try {
            updateInterest("addinterests");
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
                    Log.i(TAG, response.toString());
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

    public void populatePossibleInterests(){
        RequestHttp requestHttp = RequestHttp.getRequestHttp();
        requestHttp.getRequest(view.getContext(), "tags", "", "", new RequestHttp.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    Log.i(TAG, response.toString());
                    for(int i = 0; i < response.length(); i++){
                        mUser.addUserInterest(((JSONObject)response.get(i)).getString("alias"));
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

    private void searchInit() {
        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
