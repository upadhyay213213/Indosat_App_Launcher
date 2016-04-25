package com.evol.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.evol.homeLauncher.HomeScreenFragmentOne;
import com.evol.homeLauncher.HomeScreenFragmentThree;
import com.evol.homeLauncher.HomeScreenMainActivity;
import com.evol.homeLauncher.HomeScreenFragmentTwo;

/**
 * Created by katakam on 1/20/2016.
 */
public class HomeScreenViewPagerAdapter extends FragmentPagerAdapter {
    int mSize;
    Activity mHomeActivity;

    public HomeScreenViewPagerAdapter(FragmentManager fragmentManager,HomeScreenMainActivity mainActivity, int noofsize) {
        super(fragmentManager);
        mSize = noofsize;
        mHomeActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {

        android.support.v4.app.Fragment fragment = null;

        if (position < mSize) {
            switch (position) {
                case 0:
                    fragment = HomeScreenFragmentOne.newInstance(mHomeActivity, position);
                    break;
                case 1:
                    fragment = HomeScreenFragmentTwo.newInstance(mHomeActivity, position);
                    break;
                case 2:
                    fragment = HomeScreenFragmentThree.newInstance(mHomeActivity, position);
                    break;
            }
            return fragment;
        } else
            return null;
    }
}
