package com.reyesc.whatdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;

import static com.reyesc.whatdo.LoginActivity.ACCESS_TOKEN;
import static com.reyesc.whatdo.LoginActivity.USER_NAME;

public class FragmentProfile extends FragmentExtension {
    private AccessToken mAccessToken;
    private String name;
    private Button mLogoutButton;
    private TextView log;
    private Profile mProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle args = getArguments();
        mAccessToken = (AccessToken)args.get(ACCESS_TOKEN);
        name = (String)args.get(USER_NAME);
        System.out.println(name);
        mProfile = Profile.getCurrentProfile();

        mLogoutButton = view.findViewById(R.id.logout_button);
        log = view.findViewById(R.id.profile_log);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        setLog(mAccessToken);

        return view;
    }

    public void setLog(AccessToken mAccessToken) {
        if (mAccessToken != null) {
            log.setText("Welcome "
                    + "\nyou are logged in "
                    + mProfile.getName().toString()
                    + "\ngranted permissions: "
                    + mAccessToken.getPermissions().toString()
                    + "\ndeclined permissions: "
                    + mAccessToken.getDeclinedPermissions().toString()
                    +"\nuser id: "
                    + mAccessToken.getUserId());
        } else {
            log.setText("you are logged out");
        }
    }
}
