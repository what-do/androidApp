package com.reyesc.whatdo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    public final static String USER = "com.reyesc.whatdo.USER";
    public final static String IMG = "com.reyesc.whatdo.IMG";

    private GoogleSignInClient mGoogleSignInClient;

    private SignInButton mSignInButton;
    private TextView userPrompt;

    private Bundle args;
    private Boolean signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        args = getIntent().getExtras();

        if (args != null) {
            signOut = args.getBoolean("signout");
        } else {
            signOut = false;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_whatdotitle);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userPrompt = findViewById(R.id.user_prompt);
        mSignInButton = findViewById(R.id.login_button);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        userPrompt.setVisibility(View.GONE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    private User createUser(GoogleSignInAccount account) {
        String id = account.getId();
        String email = account.getEmail();
        String username = account.getDisplayName();
        Uri img = account.getPhotoUrl();
        Log.i(TAG, img.toString());
        return User.getInstance(id, email, username);
    }

    private void handleSignInResult (Task<GoogleSignInAccount> completedTask) {
        try  {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            userPrompt.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            mSignInButton.setVisibility(View.VISIBLE);
            mSignInButton.setSize(mSignInButton.SIZE_STANDARD);
        } else {
            if (signOut) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                signOut = false;
                            }
                        });
            } else {
                mSignInButton.setVisibility(View.GONE);
                User user = createUser(account);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(USER, user);
                intent.putExtra(IMG, account.getPhotoUrl());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                signIn();
                break;
        }
    }
}
