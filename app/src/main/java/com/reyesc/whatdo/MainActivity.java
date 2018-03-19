package com.reyesc.whatdo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    replaceFragment(new ProfileFragment());
                    return true;
                case R.id.navigation_discover:
                    replaceFragment(new DiscoverFragment());
                    return true;
                case R.id.navigation_saved:
                    replaceFragment(new SavedFragment());
                    return true;
                case R.id.navigation_friends:
                    replaceFragment(new FriendsFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_whatdotitle);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        replaceFragment(new DiscoverFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setSelectedItemId(R.id.navigation_discover);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
