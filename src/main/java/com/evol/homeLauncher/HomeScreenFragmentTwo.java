package com.evol.homeLauncher;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.evol.promotionalApp.DialogParentActivity;
import com.evol.promotionalApp.IndosatAppIntentReceiver;
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
public class HomeScreenFragmentTwo extends Fragment implements IndosatAppIntentReceiver.AppInstalledListener{

    static final String TAG = "HomeScreenFragmentTwo";

    ViewGroup mainlayout;
    private Context context;
    private View view;
    private static int REQUEST_PICK_APPWIDGET = 111;
    private static int REQUEST_CREATE_APPWIDGET = 222;
    private static int APPWIDGET_HOST_ID = 333;

    AppWidgetManager mAppWidgetManager;
    LauncherAppWidgetHost mAppWidgetHost;

    RelativeLayout mHomeScreenApps;

    ImageView app1;
    ImageView app2;
    ImageView app3;
    ImageView app4;
    ImageView app5;
    ImageView app6;
    ImageView app7;
    ImageView app8;

    ImageView app1_trans;
    ImageView app2_trans;
    ImageView app3_trans;
    ImageView app4_trans;
    ImageView app5_trans;
    ImageView app6_trans;
    ImageView app7_trans;
    ImageView app8_trans;

    private PromotionalAppSharedPreferences pref;

    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();
    boolean rotateFlag = false;

    LinearLayout firstRow, secondRow, firstRowTransparent, secondRowTransparent;
    private static HomeScreenFragmentTwo currentFragment;

    public static HomeScreenFragmentTwo newInstance(Activity homeActivity, int position) {

        currentFragment = new HomeScreenFragmentTwo();
        Bundle b = new Bundle();
        b.putInt("MSG", position);
        currentFragment.setArguments(b);
        return currentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.home_widget_host_fragment_layout_1, container, false);

        context = view.getContext();

        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetHost = new LauncherAppWidgetHost(context, APPWIDGET_HOST_ID);

        loadViews(view);
        updateSharedPreferencesAndWidget();
        createThisAppShortCut();
        IndosatAppIntentReceiver.setListener(this);
        return view;
    }

    private void loadViews(View view) {

        mHomeScreenApps = (RelativeLayout) view.findViewById(R.id.home_screen_apps_main);

        firstRow = (LinearLayout) mHomeScreenApps.findViewById(R.id.top_row_apps_ll);
        secondRow = (LinearLayout) mHomeScreenApps.findViewById(R.id.second_row_apps_ll);
        firstRowTransparent = (LinearLayout) mHomeScreenApps.findViewById(R.id.top_row_apps_transparent_ll);
        secondRowTransparent = (LinearLayout) mHomeScreenApps.findViewById(R.id.second_row_apps_transparent_ll);
        secondRow.setVisibility(View.GONE);
        secondRowTransparent.setVisibility(View.GONE);

        app1 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app1);
        app2 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app2);
        app3 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app3);
        app4 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app4);
        app5 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app5);
        app6 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app6);
        app7 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app7);
        app8 = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app8);

        app1_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app1_transparent);
        app2_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app2_transparent);
        app3_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app3_transparent);
        app4_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app4_transparent);
        app5_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app5_transparent);
        app6_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app6_transparent);
        app7_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app7_transparent);
        app8_trans = (ImageView) mHomeScreenApps.findViewById(R.id.widget_app8_transparent);

        loadImageIcons();

        app1.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app2.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app3.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app4.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app5.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app6.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app7.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());
        app8.setOnClickListener(new HomeScreenPromotionalAppsClickEventClass());

        mainlayout = (ViewGroup) view.findViewById(R.id.home_view_1);
        mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showLongPressOptions();
                return false;
            }
        });
    }

    private void loadImageIcons() {
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(0), app1, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(1), app2, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(2), app3, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(3), app4, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(4), app5, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(5), app6, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(6), app7, AppUtils.getDefaultDisplayOptionsActive());
        ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(7), app8, AppUtils.getDefaultDisplayOptionsActive());
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 3000, 3000);//AppConstants.ROTATE_APP_INITIAL_DURATION, AppConstants.ROTATE_APP_DURATION);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        changeHoemScreenAppsLayout(rotateFlag);
                    }
                });
            }
        };
    }

    private void changeHoemScreenAppsLayout(boolean flag) {
        if (flag) {
            secondRow.setVisibility(View.GONE);
            secondRowTransparent.setVisibility(View.GONE);
            firstRow.setVisibility(View.VISIBLE);
            firstRowTransparent.setVisibility(View.VISIBLE);
        } else {
            firstRow.setVisibility(View.GONE);
            firstRowTransparent.setVisibility(View.GONE);
            secondRow.setVisibility(View.VISIBLE);
            secondRowTransparent.setVisibility(View.VISIBLE);
        }
        rotateFlag = !flag;
    }

    private void createThisAppShortCut() {
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name).toString());

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.indosat_logo)).getBitmap();
        shortcutintent.putExtra("ICON", bitmap);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context, DialogParentActivity.class));
        createAppShortCut(context, shortcutintent);
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
                LinearLayout.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM);
        LLParams.gravity = Gravity.END;
        LLParams.bottomMargin = 20;

        ImageView tempShortCut = new ImageView(context);
        int width = (int) getContext().getResources().getDimension(R.dimen.app_icon_size);
        int height = (int) getContext().getResources().getDimension(R.dimen.app_icon_size);
        LinearLayout.LayoutParams ivParms = new LinearLayout.LayoutParams(width,height);
        tempShortCut.setLayoutParams(ivParms);
        tempShortCut.setImageBitmap(icon);

        TextView shortCutTitle = new TextView(context);
        shortCutTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

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
                startWallpaper();
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
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }  else {
                Toast.makeText(context, "NO MATCHING REQUEST CODE : " + requestCode, Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
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

    @Override
    public void onMessageReceived() {
        updateSharedPreferencesAndWidget();
    }

    class HomeScreenPromotionalAppsClickEventClass implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String uri1 = "";
            String uri2 = "";

            switch (view.getId()) {

                case R.id.widget_app1:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(0);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(0);
                    break;
                case R.id.widget_app2:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(1);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(1);
                    break;
                case R.id.widget_app3:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(2);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(2);
                    break;
                case R.id.widget_app4:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(3);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(3);
                    break;
                case R.id.widget_app5:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(4);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(4);
                    break;
                case R.id.widget_app6:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(5);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(5);
                    break;
                case R.id.widget_app7:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(6);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(6);
                    break;
                case R.id.widget_app8:
                    uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(7);
                    uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(7);
                    break;
                default:
                    break;
            }
            downloadApp(uri1, uri2);
        }
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

    public void updateSharedPreferencesAndWidget() {

        pref = PromotionalAppSharedPreferences.getInstance(context.getApplicationContext());

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0))) {
            pref.setAPP1InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0)));
            app1_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP1InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(0),
                    app1_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(1))) {
            pref.setAPP2InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(1)));
            app2_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP2InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(1)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(1),
                    app2_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(2))) {
            pref.setAPP3InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(2)));
            app3_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP3InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(2)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(2),
                    app3_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(3))) {
            pref.setAPP4InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(3)));
            app4_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP4InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(3)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(3),
                    app4_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(4))) {
            pref.setAPP5InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(4)));
            app5_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP5InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(4)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(4),
                    app5_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(5))) {
            pref.setAPP6InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(5)));
            app6_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP6InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(5)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(5),
                    app6_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(6))) {
            pref.setAPP7InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(6)));
            app7_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP7InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(6)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(6),
                    app7_trans, AppUtils.getDefaultDisplayOptionsActive());
        }

        if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(7))) {
            pref.setAPP8InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(7)));
            app8_trans.setImageResource(R.drawable.installed_app);
        } else {
            pref.setAPP8InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(7)));
            ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(7),
                    app8_trans, AppUtils.getDefaultDisplayOptionsActive());
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
        stoptimertask();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stoptimertask();
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

    private void startWallpaper() {
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(pickWallpaper, getString(R.string.menu_wallpaper)));
    }
}

