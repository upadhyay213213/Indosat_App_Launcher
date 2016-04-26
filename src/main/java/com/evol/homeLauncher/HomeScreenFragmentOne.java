package com.evol.homeLauncher;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evol.appUtils.AppConstants;
import com.evol.appUtils.AppUtils;
import com.evol.launcher_lib.TweakDashboardView;
import com.evol.promotionalApp.DialogParentActivity;
import com.evol.promotionalApp.PromotionalAppSharedPreferences;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by katakam on 1/21/2016.
 */
public class HomeScreenFragmentOne extends HomeFragment {

    static final String TAG = "HomeScreenFragmentOne";



    ViewGroup mainlayout;
    private Context context;
    private View view;
    private static int REQUEST_PICK_APPWIDGET = 111;
    private static int REQUEST_CREATE_APPWIDGET = 222;
    private static int APPWIDGET_HOST_ID = 333;

    AppWidgetManager mAppWidgetManager;
    LauncherAppWidgetHost mAppWidgetHost;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    private PromotionalAppSharedPreferences pref;



    public static HomeScreenFragmentOne newInstance(Activity homeActivity, int position) {
        HomeScreenFragmentOne currentFragment = new HomeScreenFragmentOne();
        Bundle b = new Bundle();
        b.putInt(INTENT_ITEM_POSITION, position);
        currentFragment.setArguments(b);
        return currentFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.home_widget_host_fragment_layout_0, container, false);

        context = view.getContext();
        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetHost = new LauncherAppWidgetHost(context, APPWIDGET_HOST_ID);
        loadViews(view);
        //addBroadcastReceiverForShortCut();

        return view;
    }

    private void loadViews(View view) {
        mainlayout = (ViewGroup) view.findViewById(R.id.home_view_0);
        mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showLongPressOptions();
                return false;
            }
        });

        initDashboardViews(view);
    }

    private void addBroadcastReceiverForShortCut() {
        mIntentFilter = new IntentFilter("com.android.launcher.action.INSTALL_SHORTCUT");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context broadCastContext, Intent intent) {
                createAppShortCut(broadCastContext, intent);
            }
        };
    }

    private void createAppShortCut(Context broadCastContext, Intent appIntent) {

        Bundle extras = appIntent.getExtras();
        final Intent currentIntent = (Intent) extras.get(Intent.EXTRA_SHORTCUT_INTENT);
        currentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String shortcut_name = extras.getString(Intent.EXTRA_SHORTCUT_NAME);
        Bitmap icon = (Bitmap) appIntent.getParcelableExtra("ICON");

        final LinearLayout shortcut = new LinearLayout(context);
        shortcut.setOrientation(LinearLayout.VERTICAL);
        shortcut.setPadding(5, 5, 5, 5);

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView tempShortCut = new ImageView(context);
        tempShortCut.setImageBitmap(icon);

        TextView shortCutTitle = new TextView(context);
        shortCutTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        ;
        shortCutTitle.setText(shortcut_name);

        shortcut.addView(tempShortCut);
        shortcut.addView(shortCutTitle);
        shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(currentIntent);
            }
        });

        shortcut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog shortCutDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);
                shortCutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shortCutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                shortCutDialog.setContentView(R.layout.widget_long_press_options);
                shortCutDialog.findViewById(R.id.delete_widget_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shortCutDialog.dismiss();
                        mainlayout.removeView(shortcut);
                    }
                });
                shortCutDialog.findViewById(R.id.cancel_widget_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shortCutDialog.dismiss();
                    }
                });

                shortCutDialog.show();
                return true;
            }
        });

        mainlayout.addView(shortcut, LLParams);

    }

    void showLongPressOptions() {
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);
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
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        dialog.show();
    }

    /**
     * Launches the menu to select the widget. The selected widget will be on
     * the result of the activity.
     */
    void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId); //tem: it should be between 0-65535
        addEmptyData(pickIntent);
        //Toast.makeText(context, "Select Widget", Toast.LENGTH_SHORT).show();
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    /**
     * This avoids a bug in the com.android.settings.AppWidgetPickActivity,
     * which is used to select widgets. This just adds empty extras to the
     * intent, avoiding the bug.
     * <p/>
     * See more: http://code.google.com/p/android/issues/detail?id=4272
     */
    void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

    /**
     * If the user has selected an widget, the result will be in the 'data' when
     * this function is called.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Toast.makeText(context, "Fragment Result OKAY", Toast.LENGTH_SHORT).show();
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                //Toast.makeText(context, "REQUEST_PICK_APPWIDGET : configureWidget", Toast.LENGTH_SHORT).show();
                configureWidget(data);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                //Toast.makeText(context, "REQUEST_CREATE_APPWIDGET : createWidget", Toast.LENGTH_SHORT).show();
                createWidget(data);
            } /*else if (requestCode == LONG_PRESS_OPTIONS) {
                Toast.makeText(context, "LONG_PRESS_OPTIONS : Result", Toast.LENGTH_SHORT).show();
            } */ else {
                Toast.makeText(context, "NO MATCHING REQUEST CODE : " + requestCode, Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            //Toast.makeText(context, "appWidgetId : " + appWidgetId, Toast.LENGTH_SHORT).show();
            if (appWidgetId != -1) {
                //Toast.makeText(context, "Delete App widget ID", Toast.LENGTH_SHORT).show();
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    /**
     * Checks if the widget needs any configuration. If it needs, launches the
     * configuration activity.
     */
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

    /**
     * Creates the widget and adds to our view layout.
     */
    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        final AppWidgetHostView hostView = mAppWidgetHost.createView(context, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);

        hostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        hostView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog widgetDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);
                widgetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                widgetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                widgetDialog.setContentView(R.layout.widget_long_press_options);
                widgetDialog.findViewById(R.id.delete_widget_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        widgetDialog.dismiss();
                        removeWidget(hostView);
                    }
                });
                widgetDialog.findViewById(R.id.cancel_widget_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        widgetDialog.dismiss();
                    }
                });

                widgetDialog.show();
                return false;
            }
        });

        mainlayout.addView(hostView);
    }

    private void downloadApp(String uri1, String uri2) {
        Intent playStoreIntent;
        try {
            playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri1));
            playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(playStoreIntent);
        } catch (android.content.ActivityNotFoundException anfe) {
            playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri2));
            playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(playStoreIntent);
        }
    }

    /**
     * Registers the AppWidgetHost to listen for updates to any widgets this app
     * has.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }

    /**
     * Stop listen for updates for our widgets (saving battery).
     */
    @Override
    public void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        //context.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        //context.unregisterReceiver(mReceiver);
    }


    /**
     * Removes the widget displayed by this AppWidgetHostView.
     */
    public void removeWidget(AppWidgetHostView hostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
        mainlayout.removeView(hostView);
    }

    /**
     * Handles the menu.
     */
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

    /**
     * Handle the 'Remove Widget' menu.
     */
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
}

