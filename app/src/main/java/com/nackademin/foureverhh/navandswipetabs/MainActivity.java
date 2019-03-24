package com.nackademin.foureverhh.navandswipetabs;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.system.Os;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.lang.System;

public class MainActivity extends AppCompatActivity
{
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            Os.setenv("GOOGLE_APPLICATION_CREDENTIALS",
                    "/Users/foureverhh/.config/gcloud/application_default_credentials.json",
                    true);
        } catch (ErrnoException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.nav_bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navBottomListener);
        setFragment(new PhotoFragment());

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navBottomListener=
    new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.nav_photo:
                    fragment = new PhotoFragment();
                    setFragment(fragment);
                    break;
                case R.id.nav_text:
                    getSupportActionBar().show();
                    fragment = new TextFragment();
                    setFragment(fragment);
                    break;
                case R.id.nav_history:
                    getSupportActionBar().show();
                    fragment = new HistoryFragment();
                    setFragment(fragment);
                    break;
            }
            return true;
        }
    };

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }
}



