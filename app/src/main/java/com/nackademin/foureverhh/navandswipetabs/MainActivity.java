package com.nackademin.foureverhh.navandswipetabs;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    TabLayout tabLayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        sectionsPagerAdapter =new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);

        viewPager.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout
                .ViewPagerOnTabSelectedListener(viewPager));

        bottomNavigationView = findViewById(R.id.nav_bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navBottomListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBottomListener=
    new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.nav_photo:

                    break;
                case R.id.nav_text:

                    break;
                case R.id.nav_history:

                    break;
            }
            return true;
        }
    };

    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}



