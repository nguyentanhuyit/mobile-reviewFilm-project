package com.example.newprojectmobileapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.newprojectmobileapp.fragments.ExploreFragment;
import com.example.newprojectmobileapp.fragments.HomeFragment;
import com.example.newprojectmobileapp.fragments.NewsFragment;
import com.example.newprojectmobileapp.fragments.UtilsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ExploreFragment();
            case 2:
                return new NewsFragment();
            case 3:
                return new UtilsFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
