package com.example.newprojectmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.newprojectmobileapp.adapters.ViewPagerAdapter;
import com.example.newprojectmobileapp.controller.ToastNotification;
import com.example.newprojectmobileapp.fragments.HomeFragment;
import com.example.newprojectmobileapp.fragments.MovieDetailFragment;
import com.example.newprojectmobileapp.model.UserAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;

    String currentUserName;
    String currentUserEmail;

    String sendName;
    String sendEmail;

    UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar);
        View customActionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(customActionBarView);
        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0,0);

//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setLogo(R.drawable.fargo_height_120);
//        actionBar.setDisplayUseLogoEnabled(true);
//        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        viewPager = findViewById(R.id.view_pager);

        setUpViewPager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_tab1:
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.menu_tab2:
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.menu_tab3:
                        viewPager.setCurrentItem(2);
                        break;

                    case R.id.menu_tab4:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserName = currentUser.getDisplayName(); /////////////////////////////////////////
            currentUserEmail = currentUser.getEmail();
//            ToastNotification.setContent(MainActivity.this, "Hello " + currentUserEmail);
        } else {
            ToastNotification.setContent(MainActivity.this, "User null");
        }
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab1).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab2).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab3).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab4).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public UserAccount getUserAccount() {

        if (currentUserName != null) {
            sendName = currentUserName;
        } else {
            sendName = "Phan Le Huy haha";
        }
        sendEmail = currentUserEmail;

        userAccount = new UserAccount(sendEmail, sendName);

        return userAccount;
    }
}