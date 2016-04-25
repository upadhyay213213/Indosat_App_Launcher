package com.evol.homeLauncher;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evol.adapters.HomeScreenViewPagerAdapter;
import com.evol.appUtils.AppConstants;
import com.evol.promotionalApp.PromotionalAppSharedPreferences;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HomeScreenMainActivity extends FragmentActivity implements View.OnClickListener {
    /**
     * Tag used for logging errors.
     */
    private static final String LOG_TAG = "HomeScreenMainActivity";

    /**
     * Keys during freeze/thaw.
     */
    private static final String KEY_SAVE_GRID_OPENED = "grid.opened";

    private static final String DEFAULT_FAVORITES_PATH = "etc/favorites.xml";

    private static final String TAG_FAVORITES = "favorites";
    private static final String TAG_FAVORITE = "favorite";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_CLASS = "class";


    private static final String DEBUG_TAG = "Contact List";
    private static final int RESULT_OK = -1;

    // Identifiers for option menu items
    private static final int MENU_WALLPAPER_SETTINGS = Menu.FIRST + 1;
    private static final int MENU_SEARCH = MENU_WALLPAPER_SETTINGS + 1;
    private static final int MENU_SETTINGS = MENU_SEARCH + 1;

    /**
     * Maximum number of recent tasks to query.
     */
    private static final int MAX_RECENT_TASKS = 20;

    private static boolean mWallpaperChecked;
    private static ArrayList<ApplicationInfo> mApplications;
    private static LinkedList<ApplicationInfo> mFavorites;

    private final BroadcastReceiver mWallpaperReceiver = new WallpaperIntentReceiver();
    private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();

    private GridView mGrid;
    private ImageView mTaskBarImage1, mTaskBarImage2, mTaskBarImage3, mTaskBarImage4, mTaskBarImage5;

    private LayoutAnimationController mShowLayoutAnimation;
    private LayoutAnimationController mHideLayoutAnimation;

    private ViewPager mHomeScreenViewPager;

    private boolean mBlockAnimation;

    private boolean mHomeDown;
    private boolean mBackDown;
    private boolean mShowingGrid;

    //private ApplicationsStackLayout mApplicationsStack;

    private Animation mGridEntry;
    private Animation mGridExit;

    private List<ImageView> dots;

    private Context context;

    private PromotionalAppSharedPreferences pref;

    AppWidgetManager mAppWidgetManager;
    LauncherAppWidgetHost mAppWidgetHost;
    int numwidgets;
    GridLayout.LayoutParams layoutParams;
    HomeScreenViewPagerAdapter adapter;
    private WallpaperManager wallpaperManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        setContentView(R.layout.home);

        context = this;

        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mAppWidgetHost = new LauncherAppWidgetHost(context, AppConstants.APPWIDGET_HOST_ID);

        pref = PromotionalAppSharedPreferences.getInstance(context.getApplicationContext());

        if(pref.isFirstRun()) {
            //Toast.makeText(context,"First Run",Toast.LENGTH_SHORT).show();
            pref.setFirstRun(false);
            //registerDevice();
        } else {
            //Toast.makeText(context,"Not First Run",Toast.LENGTH_SHORT).show();
        }

        registerIntentReceivers();
        setDefaultWallpaper();
        loadApplications(true);
        bindApplications();
        //bindFavorites(true);
        //bindRecents();
        bindButtons();
        loadHomeScreenFragmentPages();
        mGridEntry = AnimationUtils.loadAnimation(this, R.anim.grid_entry);
        mGridExit = AnimationUtils.loadAnimation(this, R.anim.grid_exit);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            getWindow().closeAllPanels();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the callback for the cached drawables or we leak
        // the previous HomeScreenMainActivity screen on orientation change
        final int count = mApplications.size();
        for (int i = 0; i < count; i++) {
            mApplications.get(i).icon.setCallback(null);
        }

        unregisterReceiver(mWallpaperReceiver);
        unregisterReceiver(mApplicationsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bindRecents();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        final boolean opened = state.getBoolean(KEY_SAVE_GRID_OPENED, false);
        if (opened) {
            showApplications(false);
            hideHomeScreenPages();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SAVE_GRID_OPENED, mGrid.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.CONTACT_PICKER_RESULT:
                    if (data != null) {
                        Uri contactUri = data.getData();
                    }
                    Log.w(DEBUG_TAG, "Warning: activity result is ok!");
                    break;
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

    private void loadHomeScreenFragmentPages() {
        adapter = new HomeScreenViewPagerAdapter(getSupportFragmentManager(),HomeScreenMainActivity.this,
                AppConstants.NUMBER_OF_HOME_SCREENS);
        mHomeScreenViewPager = (ViewPager) findViewById(R.id.home_screen_pager);
        mHomeScreenViewPager.setAdapter(adapter);
        mHomeScreenViewPager.setOffscreenPageLimit(AppConstants.NUMBER_OF_HOME_SCREENS);
        mHomeScreenViewPager.setCurrentItem(AppConstants.CURRENT_HOME_FRAGMENT);

        loadDots();

        selectDot(AppConstants.CURRENT_HOME_FRAGMENT);

        mHomeScreenViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                AppConstants.CURRENT_HOME_FRAGMENT = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.launcher_taskbar_image1:
                invokeDialer();
                break;
            case R.id.launcher_taskbar_image2:
                openContect();
                break;
            case R.id.launcher_taskbar_image3:
                if (mShowingGrid) {
                    hideApplications();
                } else {
                    showApplications(true);
                }
                break;
            case R.id.launcher_taskbar_image4:
                openMessages();
                break;
            case R.id.launcher_taskbar_image5:
                openCamera();
                break;
            default:
                break;
        }

    }

    private void loadDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)findViewById(R.id.dots);

        for(int i = 0; i < AppConstants.NUMBER_OF_HOME_SCREENS; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.unselected_gray_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10,10,10,10);

            dotsLayout.addView(dot, params);
            dots.add(dot);
        }
    }

    private void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < AppConstants.NUMBER_OF_HOME_SCREENS; i++) {
            int drawableId = (i==idx)?(R.drawable.selected_gray_dot):(R.drawable.unselected_gray_dot);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    /**
     * Registers various intent receivers. The current implementation registers
     * only a wallpaper intent receiver to let other applications change the
     * wallpaper.
     */
    private void registerIntentReceivers() {
        wallpaperManager = WallpaperManager.getInstance(context);
        IntentFilter filter = new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED);
        registerReceiver(mWallpaperReceiver, filter);

        filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mApplicationsReceiver, filter);
    }

    /**
     * Creates a new appplications adapter for the grid view and registers it.
     */
    private void bindApplications() {
        if (mGrid == null) {
            mGrid = (GridView) findViewById(R.id.all_apps);
        }
        mGrid.setAdapter(new ApplicationsAdapter(this, mApplications));
        mGrid.setSelection(0);

        if (mTaskBarImage1 == null) {
            mTaskBarImage1 = (ImageView) findViewById(R.id.launcher_taskbar_image1);
        }
        if (mTaskBarImage2 == null) {
            mTaskBarImage2 = (ImageView) findViewById(R.id.launcher_taskbar_image2);
        }
        if (mTaskBarImage3 == null) {
            mTaskBarImage3 = (ImageView) findViewById(R.id.launcher_taskbar_image3);
        }
        if (mTaskBarImage4 == null) {
            mTaskBarImage4 = (ImageView) findViewById(R.id.launcher_taskbar_image4);
        }
        if (mTaskBarImage5 == null) {
            mTaskBarImage5 = (ImageView) findViewById(R.id.launcher_taskbar_image5);
        }

        /*if (mApplicationsStack == null) {
            mApplicationsStack = (ApplicationsStackLayout) findViewById(R.id.faves_and_recents);
        }*/
    }

    /**
     * Binds actions to the various buttons.
     */
    private void bindButtons() {
        //mShowApplications = findViewById(R.id.show_all_apps);
        //mShowApplications.setOnClickListener(new ShowApplications());
        //mShowApplicationsCheck = (CheckBox) findViewById(R.id.show_all_apps_check);

        mGrid.setOnItemClickListener(new ApplicationLauncher());
        mGrid.setOnItemLongClickListener(new AppLongClickOptionLauncher());
        mTaskBarImage1.setOnClickListener(this);
        mTaskBarImage2.setOnClickListener(this);
        mTaskBarImage3.setOnClickListener(this);
        mTaskBarImage4.setOnClickListener(this);
        mTaskBarImage5.setOnClickListener(this);
    }

    /**
     * When no wallpaper was manually set, a default wallpaper is used instead.
     */
    private void setDefaultWallpaper() {
        if (!mWallpaperChecked) {
            Drawable wallpaper = wallpaperManager.peekDrawable();
            if (wallpaper == null) {
                try {
                    wallpaperManager.clear();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Failed to clear wallpaper " + e);
                }
            } else {
                getWindow().setBackgroundDrawable(new ClippedDrawable(wallpaper));
            }
            mWallpaperChecked = true;
        }
    }





    private static void beginDocument(XmlPullParser parser, String firstElementName)
            throws XmlPullParserException, IOException {

        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG &&
                type != XmlPullParser.END_DOCUMENT) {
            // Empty
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
                    ", expected " + firstElementName);
        }
    }

    private static void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG &&
                type != XmlPullParser.END_DOCUMENT) {
            // Empty
        }
    }

    private static ApplicationInfo getApplicationInfo(PackageManager manager, Intent intent) {
        final ResolveInfo resolveInfo = manager.resolveActivity(intent, 0);

        if (resolveInfo == null) {
            return null;
        }

        final ApplicationInfo info = new ApplicationInfo();
        final ActivityInfo activityInfo = resolveInfo.activityInfo;
        info.icon = activityInfo.loadIcon(manager);
        if (info.title == null || info.title.length() == 0) {
            info.title = activityInfo.loadLabel(manager);
        }
        if (info.title == null) {
            info.title = "";
        }
        return info;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            mBackDown = mHomeDown = false;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    mBackDown = true;
                    return true;
                case KeyEvent.KEYCODE_HOME:
                    mHomeDown = true;
                    return true;
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    if (!event.isCanceled() && mShowingGrid) {
                        hideApplications();
                    }
                    mBackDown = true;
                    return true;
                case KeyEvent.KEYCODE_HOME:
                    /*if (!event.isCanceled()) {
                        // Do HOME behavior.
                    }*/
                    mHomeDown = true;
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_WALLPAPER_SETTINGS, 0, R.string.menu_wallpaper)
                .setIcon(android.R.drawable.ic_menu_gallery)
                .setAlphabeticShortcut('W');
        menu.add(0, MENU_SEARCH, 0, R.string.menu_search)
                .setIcon(android.R.drawable.ic_search_category_default)
                .setAlphabeticShortcut(SearchManager.MENU_KEY);
        menu.add(0, MENU_SETTINGS, 0, R.string.menu_settings)
                .setIcon(android.R.drawable.ic_menu_preferences)
                .setIntent(new Intent(android.provider.Settings.ACTION_SETTINGS));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_WALLPAPER_SETTINGS:
                startWallpaper();
                return true;
            case MENU_SEARCH:
                onSearchRequested();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startWallpaper() {
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(pickWallpaper, getString(R.string.menu_wallpaper)));
    }

    /**
     * Loads the list of installed applications in mApplications.
     */
    private void loadApplications(boolean isLaunching) {
        if (isLaunching && mApplications != null) {
            return;
        }

        PackageManager manager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            if (mApplications == null) {
                mApplications = new ArrayList<ApplicationInfo>(count);
            }
            mApplications.clear();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);

                application.setAppFullInfo(info);
                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(
                                info.activityInfo.applicationInfo.packageName,
                                info.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = info.activityInfo.loadIcon(manager);

                mApplications.add(application);
            }
        }
    }

    private void showHomeScreenPages() {
        mHomeScreenViewPager.setVisibility(View.VISIBLE);
    }

    private void hideHomeScreenPages() {
        mHomeScreenViewPager.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows all of the applications by playing an animation on the grid.
     */
    private void showApplications(boolean animate) {
        if (mBlockAnimation) {
            return;
        }
        mBlockAnimation = true;
        mShowingGrid = true;

        // mShowApplicationsCheck.toggle();

        if (mShowLayoutAnimation == null) {
            mShowLayoutAnimation = AnimationUtils.loadLayoutAnimation(
                    this, R.anim.show_applications);
        }

        // This enables a layout animation; if you uncomment this code, you need to
        // comment the line mGrid.startAnimation() below
        mGrid.setLayoutAnimationListener(new ShowGrid());
        mGrid.setLayoutAnimation(mShowLayoutAnimation);
        mGrid.startLayoutAnimation();

        if (animate) {
            mGridEntry.setAnimationListener(new ShowGrid());
            // mGrid.startAnimation(mGridEntry);
        }

        mGrid.setVisibility(View.VISIBLE);

        if (!animate) {
            mBlockAnimation = false;
        }

        // ViewDebug.startHierarchyTracing("HomeScreenMainActivity", mGrid);
    }

    /**
     * Hides all of the applications by playing an animation on the grid.
     */
    private void hideApplications() {
        if (mBlockAnimation) {
            return;
        }
        mBlockAnimation = true;
        mShowingGrid = false;

        //mShowApplicationsCheck.toggle();

        if (mHideLayoutAnimation == null) {
            mHideLayoutAnimation = AnimationUtils.loadLayoutAnimation(
                    this, R.anim.hide_applications);
        }

        mGridExit.setAnimationListener(new HideGrid());
        mGrid.startAnimation(mGridExit);
        mGrid.setVisibility(View.INVISIBLE);
        //mShowApplications.requestFocus();

        // This enables a layout animation; if you uncomment this code, you need to
        // comment the line mGrid.startAnimation() above
//        mGrid.setLayoutAnimationListener(new HideGrid());
//        mGrid.setLayoutAnimation(mHideLayoutAnimation);
//        mGrid.startLayoutAnimation();
    }


    /**
     * Receives intents from other applications to change the wallpaper.
     */
    private class WallpaperIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getWindow().setBackgroundDrawable(new ClippedDrawable(wallpaperManager.getDrawable()));
        }
    }

    /**
     * Receives notifications when applications are added/removed.
     */
    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApplications(false);
            bindApplications();
            // bindRecents();
            // bindFavorites(false);
        }
    }

    /**
     * GridView adapter to show the list of all installed applications.
     */
    private class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {
        private Rect mOldBounds = new Rect();

        public ApplicationsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
            super(context, 0, apps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ApplicationInfo info = mApplications.get(position);

            if (convertView == null) {
                final LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.application, parent, false);
            }

            Drawable icon = info.icon;

            if (!info.filtered) {
                final Resources resources = getContext().getResources();
                int width = (int) resources.getDimension(R.dimen.app_icon_size);
                int height = (int) resources.getDimension(R.dimen.app_icon_size);

                final int iconWidth = icon.getIntrinsicWidth();
                final int iconHeight = icon.getIntrinsicHeight();

                if (icon instanceof PaintDrawable) {
                    PaintDrawable painter = (PaintDrawable) icon;
                    painter.setIntrinsicWidth(width);
                    painter.setIntrinsicHeight(height);
                }

                if (width > 0 && height > 0 && (width < iconWidth || height < iconHeight)) {
                    final float ratio = (float) iconWidth / iconHeight;

                    if (iconWidth > iconHeight) {
                        height = (int) (width / ratio);
                    } else if (iconHeight > iconWidth) {
                        width = (int) (height * ratio);
                    }

                    final Bitmap.Config c =
                            icon.getOpacity() != PixelFormat.OPAQUE ?
                                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                    final Bitmap thumb = Bitmap.createBitmap(width, height, c);
                    final Canvas canvas = new Canvas(thumb);
                    canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));
                    // Copy the old bounds to restore them later
                    // If we were to do oldBounds = icon.getBounds(),
                    // the call to setBounds() that follows would
                    // change the same instance and we would lose the
                    // old bounds
                    mOldBounds.set(icon.getBounds());
                    icon.setBounds(0, 0, width, height);
                    icon.draw(canvas);
                    icon.setBounds(mOldBounds);
                    icon = info.icon = new BitmapDrawable(getContext().getResources(), thumb);
                    info.filtered = true;
                }
            }

            final TextView textView = (TextView) convertView.findViewById(R.id.label);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
            textView.setText(info.title);

            return convertView;
        }
    }

    /**
     * Shows and hides the applications grid view.
     */
    private class ShowApplications implements View.OnClickListener {
        public void onClick(View v) {
            if (mGrid.getVisibility() != View.VISIBLE) {
                showApplications(true);
                hideHomeScreenPages();
            } else {
                hideApplications();
                showHomeScreenPages();
            }
        }
    }

    /**
     * Hides the applications grid when the layout animation is over.
     */
    private class HideGrid implements Animation.AnimationListener {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mBlockAnimation = false;
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * Shows the applications grid when the layout animation is over.
     */
    private class ShowGrid implements Animation.AnimationListener {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mBlockAnimation = false;
            // ViewDebug.stopHierarchyTracing();
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * Starts the selected activity/application in the grid view.
     */
    private class ApplicationLauncher implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
            startActivity(app.intent);
        }
    }

    /**
     * Starts the selected activity/application in the grid view.
     */
    private class AppLongClickOptionLauncher implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

            final ApplicationInfo tempAdapterView = (ApplicationInfo) adapterView.getItemAtPosition(position);

            //animateMoveAllItems();
            final Dialog appDialog = new Dialog(HomeScreenMainActivity.this, android.R.style.Theme_Black_NoTitleBar);
            appDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            appDialog.setContentView(R.layout.app_long_press_options);
            appDialog.findViewById(R.id.shortcut_app_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(HomeScreenMainActivity.this, "Shortcut", Toast.LENGTH_SHORT).show();
                    appDialog.dismiss();
                    createShortCut(tempAdapterView);
                }
            });
            appDialog.findViewById(R.id.delete_app_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(HomeScreenMainActivity.this, "Uninstalled", Toast.LENGTH_SHORT).show();
                    appDialog.dismiss();
                    uninstallApp(tempAdapterView);
                    //cancelAnimations();
                }
            });
            appDialog.findViewById(R.id.cancel_app_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(HomeScreenMainActivity.this, "Cancelled Action", Toast.LENGTH_SHORT).show();
                    appDialog.dismiss();
                    //cancelAnimations();
                }
            });
            appDialog.show();
            return true;
        }
    }

    private void animateMoveAllItems() {
        Animation rotateAnimation = createFastRotateAnimation();

        for (int i=0; i < mApplications.size(); i++) {
            View child = mGrid.getChildAt(i);
            child.startAnimation(rotateAnimation);
        }
    }

    private void cancelAnimations() {
        for (int i=0; i < mApplications.size()-2; i++) {
            View child = mGrid.getChildAt(i);
            child.clearAnimation();
        }
    }

    private Animation createFastRotateAnimation() {
        Animation rotate = new RotateAnimation(-2.0f,
                2.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(60);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());

        return rotate;
    }

    private void uninstallApp(ApplicationInfo appInfo) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + appInfo.getAppFullInfo().activityInfo.applicationInfo.packageName));
        startActivity(intent);
    }

    public void createShortCut(ApplicationInfo appInfo){

        hideApplications();

        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appInfo.title);

        Bitmap bitmap = ((BitmapDrawable)appInfo.icon).getBitmap();
        shortcutintent.putExtra("ICON", bitmap);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, appInfo.intent);//new Intent(appInfo.getAppFullInfo().activityInfo.packageName));
        sendBroadcast(shortcutintent);

        mHomeScreenViewPager.setCurrentItem(2);
    }


    private void invokeDialer() {
        startActivity(new Intent(Intent.ACTION_DIAL));
    }

    private void openContect() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivity(intent);
        /*if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CONTACT_PICKER_RESULT);
        }*/
    }

    private void openMessages() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.android.mms", "com.android.mms.ui.ConversationList"));
        startActivity(intent);
    }

    private void openCamera() {
        startActivity(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE));
    }

    /**
     * When a drawable is attached to a View, the View gives the Drawable its dimensions
     * by calling Drawable.setBounds(). In this application, the View that draws the
     * wallpaper has the same size as the screen. However, the wallpaper might be larger
     * that the screen which means it will be automatically stretched. Because stretching
     * a bitmap while drawing it is very expensive, we use a ClippedDrawable instead.
     * This drawable simply draws another wallpaper but makes sure it is not stretched
     * by always giving it its intrinsic dimensions. If the wallpaper is larger than the
     * screen, it will simply get clipped but it won't impact performance.
     */
    private class ClippedDrawable extends Drawable {
        private final Drawable mWallpaper;

        public ClippedDrawable(Drawable wallpaper) {
            mWallpaper = wallpaper;
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            // Ensure the wallpaper is as large as it really is, to avoid stretching it
            // at drawing time
            mWallpaper.setBounds(left, top, left + mWallpaper.getIntrinsicWidth(),
                    top + mWallpaper.getIntrinsicHeight());
        }

        public void draw(Canvas canvas) {
            mWallpaper.draw(canvas);
        }

        public void setAlpha(int alpha) {
            mWallpaper.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            mWallpaper.setColorFilter(cf);
        }

        public int getOpacity() {
            return mWallpaper.getOpacity();
        }
    }


}
