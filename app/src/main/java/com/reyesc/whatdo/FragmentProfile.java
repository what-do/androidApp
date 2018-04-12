package com.reyesc.whatdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentProfile extends FragmentExtension {

    private static CallbackManager callbackManager;
    private static LoginButton loginButton;
    private static TextView log;
    private static boolean loggedIn = false;
    private static final String EMAIL = "email";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        log = (TextView)view.findViewById(R.id.profile_log);

        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                log.setText(loginResult.getRecentlyGrantedPermissions().toString()
                        + "\n"
                        + loginResult.getAccessToken().toString());
                loggedIn = true;
            }

            @Override
            public void onCancel() {
                log.setText("canceled");

            }

            @Override
            public void onError(FacebookException error) {
                log.setText("error");
            }
        });

        if (loggedIn) {
            log.setText("user profile");
        }
        else {
            log.setText("please loging to view user profile");
        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
