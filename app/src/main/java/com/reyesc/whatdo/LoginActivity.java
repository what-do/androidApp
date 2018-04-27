package com.reyesc.whatdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    public final static String ACCESS_TOKEN = "com.reyesc.whatdo.ACCESS_TOKEN";
    public final static String USER_NAME = "com.reyesc.whatdo.USER_NAME";
    //read permissions

    //permissions that do NOT require FB Review
    private static final String EMAIL = "email";
    private static final String PUB_PROF = "public_profile";

    //permissions that require FB Review
    private static final String USER_LOC = "user_location";
    private static final String USER_BDAY = "user_birthday";
    private static final String USER_FRIENDS = "user_friends";
    private static final String USER_EVENTS = "user_events";

    private CallbackManager mCallbackManager;
    private AccessToken mAccessToken;
    private LoginButton mLoginButton;
    private ProgressBar mProgressBar;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_whatdotitle);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setContentView(R.layout.activity_login);

        mProgressBar =findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = findViewById(R.id.login_button);
        mAccessToken = AccessToken.getCurrentAccessToken();
        loggedIn = isLoggedIn(mAccessToken);

        if (loggedIn) {
            System.out.println("already logged in");
            loginHandler(mAccessToken);
        } else {
            mLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUB_PROF, USER_EVENTS, USER_BDAY, USER_LOC));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    System.out.println("login successful");
                    mLoginButton.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAccessToken = loginResult.getAccessToken();
                    loggedIn = isLoggedIn(mAccessToken);
                    if (loggedIn) {
                        loginHandler(mAccessToken);
                    } else {
                        System.out.println("error");
                    }
                }

                @Override
                public void onCancel() {
                    System.out.println("login canceled");
                }

                @Override
                public void onError(FacebookException error) {
                    System.out.println("login error");

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginHandler(AccessToken mAccessToken) {
        String user_id = mAccessToken.getUserId().toString();
        System.out.println("user id: " + user_id);
        final String[] name = {""};

        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMeRequest(
                        mAccessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                System.out.println("in first request");

                                try {
                                    System.out.println(jsonObject.toString());
                                    name[0] = (String)jsonObject.get("name");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ),
                new GraphRequest (
                        mAccessToken,
                        "/" + user_id + "/events",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                System.out.println("in second request");
                                JSONObject jsonObject = response.getJSONObject();
                                JSONArray jsonArray = response.getJSONArray();
                                try {
                                    System.out.println(jsonObject.toString());
                                    System.out.println(jsonArray.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                System.out.println("batch completed");
            }
        });
        batch.executeAsync();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ACCESS_TOKEN, mAccessToken);
        intent.putExtra(USER_NAME, name[0]);
        System.out.println("attempting to send user info\nuser id: "
                + mAccessToken.getUserId().toString());
        startActivity(intent);
    }

    private boolean isLoggedIn(AccessToken mAccessToken) {
        if (mAccessToken != null) {
            return true;
        } else {
            return false;
        }
    }

    //TODO: permissions switches
    /**
    private void setPermissionSwitches() {
    }
     */
}
