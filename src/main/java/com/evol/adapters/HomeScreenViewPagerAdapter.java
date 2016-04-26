package com.evol.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.evol.homeLauncher.HomeScreenFragmentOne;
import com.evol.homeLauncher.HomeScreenFragmentThree;
import com.evol.homeLauncher.HomeScreenMainActivity;
import com.evol.homeLauncher.HomeScreenFragmentTwo;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by katakam on 1/20/2016.
 */
public class HomeScreenViewPagerAdapter
        extends FragmentPagerAdapter
        implements IconPagerAdapter {


    int mSize;
    Activity mHomeActivity;

    protected static final String[] CONTENT = new String[]{"Board1", "Board2", "Board3", "Board4"};

    private Fragment[] fragments = new Fragment[] {
            HomeScreenFragmentOne.newInstance(mHomeActivity, 0),
            HomeScreenFragmentTwo.newInstance(mHomeActivity, 1),
            HomeScreenFragmentThree.newInstance(mHomeActivity, 2)};

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
            return fragments[position];
        } else
            return null;
    }

    @Override
    public int getIconResId(int index) {
        return -1;
    }


}
