package com.reyesc.whatdo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragmentStack;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    replaceFragment(R.id.fragment_profile);
                    return true;
                case R.id.navigation_discover:
                    replaceFragment(R.id.fragment_discover);
                    return true;
                case R.id.navigation_saved:
                    replaceFragment(R.id.fragment_saved);
                    return true;
                case R.id.navigation_friends:
                    replaceFragment(R.id.fragment_friends);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentStack = new ArrayList<Fragment>();
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_whatdotitle);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        replaceFragment(R.id.fragment_discover);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setSelectedItemId(R.id.navigation_discover);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void replaceFragment(int fragmentTag) {
        Fragment currentFragment = null;

        if (fragmentStack.size() > 0) {
            currentFragment = fragmentStack.get(fragmentStack.size() - 1);
            boolean onBackStack = false;
            Fragment fragment = null;

            for (Fragment frag : fragmentStack) {
                if (fragmentTag == Integer.parseInt(frag.getTag())) {
                    onBackStack = true;
                    fragment = frag;
                    break;
                }
            }

            if (onBackStack) {
                if (fragment.equals(currentFragment)) return;
                fragmentStack.remove(fragment);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.detach(currentFragment);
                transaction.attach(fragment);
                fragmentStack.add(fragment);
                transaction.commit();
                return;
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(fragmentTag));
        if (fragment == null){
            switch (fragmentTag) {
                case R.id.fragment_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.fragment_discover:
                    fragment = new DiscoverFragment();
                    break;
                case R.id.fragment_saved:
                    fragment = new SavedFragment();
                    break;
                case R.id.fragment_friends:
                    fragment = new FriendsFragment();
                    break;
            }
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) transaction.detach(currentFragment);
        transaction.add(R.id.container, fragment, String.valueOf(fragmentTag));
        fragmentStack.add(fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            Fragment currentFragment = fragmentStack.get(fragmentStack.size() - 1);
            Fragment previousFragment = fragmentStack.get(fragmentStack.size() - 2);
            fragmentStack.remove(currentFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.detach(currentFragment);
            transaction.attach(previousFragment);
            transaction.commit();

            BottomNavigationView view = (BottomNavigationView)this.findViewById(R.id.navigationView);
            switch (Integer.parseInt(previousFragment.getTag())) {
                case R.id.fragment_profile:
                    view.setSelectedItemId(R.id.navigation_profile);
                    break;
                case R.id.fragment_discover:
                    view.setSelectedItemId(R.id.navigation_discover);
                    break;
                case R.id.fragment_saved:
                    view.setSelectedItemId(R.id.navigation_saved);
                    break;
                case R.id.fragment_friends:
                    view.setSelectedItemId(R.id.navigation_friends);
                    break;
            }
        } else {
            this.moveTaskToBack(true);
            //this.finish();
        }
    }

    private void toasting(String string){
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();
    }
}
