package com.evol.homeLauncher;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by katakam on 1/21/2016.
 */
public class HomeScreenFragment_BACKUP extends Fragment {

    /*static final String TAG = "HomeScreenFragmentTwo";

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    ViewGroup mainlayout;
    private Context context;
    private View view;
    private static int REQUEST_PICK_APPWIDGET = 111;
    private static int REQUEST_CREATE_APPWIDGET = 222;
    private static int APPWIDGET_HOST_ID = 333;
    private static String  LONG_PRESS_FROM = "LONG_PRESS_FROM";
    private static int  LONG_PRESS_OPTIONS = 11;

    Object DragShadowBuilder;

    //AppWidgetManager mAppWidgetManager;
   // LauncherAppWidgetHost mAppWidgetHost;
    int numwidgets;

    public static HomeScreenFragment_BACKUP newInstance(Activity homeActivity, int text) {

        HomeScreenFragment_BACKUP currentFragment = new HomeScreenFragment_BACKUP();
        Bundle b = new Bundle();
        b.putInt("msg", text);
        currentFragment.setArguments(b);
        return currentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.home_widget_host_fragment_layout_1, container, false);

        context = getActivity().getApplicationContext();
        mainlayout = (ViewGroup) view.findViewById(R.id.home_view);

        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetHost = new AppWidgetHost(context, APPWIDGET_HOST_ID);

        mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showLongPressOptions();
                return false;
            }
        });

        return view;
    }

    void showLongPressOptions() {
        final Dialog dialog = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.home_screen_long_press_options);
        dialog.findViewById(R.id.widget_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                selectWidget();
            }
        });
        dialog.findViewById(R.id.wallpaper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Select Wallpaper",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Select Settings",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

     *//**
     * Launches the menu to select the widget. The selected widget will be on
     * the result of the activity.
     *//*
    void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId); //tem: it should be between 0-65535
        addEmptyData(pickIntent);
        Toast.makeText(context, "Select Widget", Toast.LENGTH_SHORT).show();
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    *//**
     * This avoids a bug in the com.android.settings.AppWidgetPickActivity,
     * which is used to select widgets. This just adds empty extras to the
     * intent, avoiding the bug.
     * <p/>
     * See more: http://code.google.com/p/android/issues/detail?id=4272
     *//*
    void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

    *//**
     * If the user has selected an widget, the result will be in the 'data' when
     * this function is called.
     *//*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(context, "Fragment Activity result : " + resultCode + "  " + Activity.RESULT_OK +
                "  " + Activity.RESULT_CANCELED, Toast.LENGTH_SHORT).show();
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Fragment Result OKAY", Toast.LENGTH_SHORT).show();
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                Toast.makeText(context, "REQUEST_PICK_APPWIDGET : configureWidget", Toast.LENGTH_SHORT).show();
                configureWidget(data);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                Toast.makeText(context, "REQUEST_CREATE_APPWIDGET : createWidget", Toast.LENGTH_SHORT).show();
                createWidget(data);
            } *//*else if (requestCode == LONG_PRESS_OPTIONS) {
                Toast.makeText(context, "LONG_PRESS_OPTIONS : Result", Toast.LENGTH_SHORT).show();
            } *//*else {
                Toast.makeText(context, "NO MATCHING REQUEST CODE : " + requestCode, Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            Toast.makeText(context, "appWidgetId : " + appWidgetId, Toast.LENGTH_SHORT).show();
            if (appWidgetId != -1) {
                Toast.makeText(context, "Delete App widget ID", Toast.LENGTH_SHORT).show();
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    *//**
     * Checks if the widget needs any configuration. If it needs, launches the
     * configuration activity.
     *//*
    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    *//**
     * Creates the widget and adds to our view layout.
     *//*
    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        AppWidgetHostView hostView = mAppWidgetHost.createView(context, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        mainlayout.addView(hostView);

        Log.i(TAG, "++++++++++++++++++++++++++++++++ The widget size is: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);
    }

    *//**
     * Registers the AppWidgetHost to listen for updates to any widgets this app
     * has.
     *//*
    @Override
    public void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }

    *//**
     * Stop listen for updates for our widgets (saving battery).
     *//*
    @Override
    public void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    *//**
     * Removes the widget displayed by this AppWidgetHostView.
     *//*
    public void removeWidget(AppWidgetHostView hostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
        mainlayout.removeView(hostView);
    }

    *//**
     * Handles the menu.
     *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu selected: " + item.getTitle() + " / " + item.getItemId() + " / " + R.id.addWidget);
        switch (item.getItemId()) {
            case R.id.addWidget:
                selectWidget();
                return true;
            case R.id.removeWidget:
                removeWidgetMenuSelected();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    *//**
     * Handle the 'Remove Widget' menu.
     *//*
    public void removeWidgetMenuSelected() {
        int childCount = mainlayout.getChildCount();
        if (childCount > 1) {
            View view = mainlayout.getChildAt(childCount - 1);
            if (view instanceof AppWidgetHostView) {
                removeWidget((AppWidgetHostView) view);
                Toast.makeText(context, R.string.widget_removed_popup, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(context, R.string.no_widgets_popup, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.widget_menu, menu);
        return true;
    }*/
}

