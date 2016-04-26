package com.evol.homeLauncher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.evol.launcher_lib.TweakDashboardView;

public class HomeFragment extends Fragment {

    protected static final String INTENT_ITEM_POSITION = "page_position";

    protected TweakDashboardView mTweakDashboardView;
    protected int mPagePos;
    protected TweakDashboardView.IViewPagerCallbacks mViewpagerCallbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mViewpagerCallbacks = (TweakDashboardView.IViewPagerCallbacks) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPagePos = getArguments().getInt(INTENT_ITEM_POSITION);
    }

    protected void initDashboardViews(View view) {
        mTweakDashboardView = (TweakDashboardView) view.findViewById(R.id.dashboard_view);
        mTweakDashboardView.setViewPagerCallbacks(mPagePos, mViewpagerCallbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_icon) {
            addAppIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAppIcon() {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mTweakDashboardView.addAppIcon(imageView);
    }

    // activity-fragment communication methods start region
    public ImageView removeAppIconFromPage(int appIconId) {
        if (mTweakDashboardView != null) {
            return mTweakDashboardView.removeAppIcon(appIconId);
        }
        return null;
    }

    public void clearIsOccupiedFlag(int cellId) {
        if (mTweakDashboardView != null) {
            mTweakDashboardView.clearIsOccupiedFlag(cellId);
        }
    }
    //endregion

}
