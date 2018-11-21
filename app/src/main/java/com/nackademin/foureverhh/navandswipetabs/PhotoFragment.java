package com.nackademin.foureverhh.navandswipetabs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    TabLayout tabLayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_photo, container, false);
        tabLayout = rootView.findViewById(R.id.tabs);
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = rootView.findViewById(R.id.viewPager);

        //tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout                         
                .ViewPagerOnTabSelectedListener(viewPager));

        viewPager.setAdapter(sectionsPagerAdapter);
        return  rootView;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment =  null;
            switch (position){
                case 0:
                    fragment = new CameraFragment();
                    break;
                case 1:
                    fragment = new ImageFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
