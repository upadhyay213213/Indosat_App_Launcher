package com.evol.appUtils;

import com.evol.homeLauncher.R;

import java.util.ArrayList;

/**
 * Created by katakam on 2/17/2016.
 */
public class AppConstants {

    /** Cache memory*/
    public static final int CACHE_MEMORY= 20 * 1024 * 1024;

    public static final int CONTACT_PICKER_RESULT = 1001;
    public static final int REQUEST_PICK_APPWIDGET = 111;
    public static final int REQUEST_CREATE_APPWIDGET = 222;
    public static final int APPWIDGET_HOST_ID = 333;

    public static final int NUMBER_OF_HOME_SCREENS = 3;
    public static int CURRENT_HOME_FRAGMENT = 1;
    public static int MAIN_HOME_FRAGMENT = 1;

    public static int ROTATE_APP_INITIAL_DURATION = 3000;
    public static int ROTATE_APP_DURATION = 3000;


    public final static String WIDGET_ACTION_APP1 = "INDOSAT.WIDGET_CLICK_ACTION.APP1";
    public final static String WIDGET_ACTION_APP2 = "INDOSAT.WIDGET_CLICK_ACTION.APP2";
    public final static String WIDGET_ACTION_APP3 = "INDOSAT.WIDGET_CLICK_ACTION.APP3";
    public final static String WIDGET_ACTION_APP4 = "INDOSAT.WIDGET_CLICK_ACTION.APP4";
    public final static String WIDGET_ACTION_APP5 = "INDOSAT.WIDGET_CLICK_ACTION.APP5";
    public final static String WIDGET_ACTION_APP6 = "INDOSAT.WIDGET_CLICK_ACTION.APP6";
    public final static String WIDGET_ACTION_APP7 = "INDOSAT.WIDGET_CLICK_ACTION.APP7";
    public final static String WIDGET_ACTION_APP8 = "INDOSAT.WIDGET_CLICK_ACTION.APP8";

    public final static ArrayList<String> INDOSAT_APPS_LIST;
    static {
        INDOSAT_APPS_LIST = new ArrayList<String>();
        INDOSAT_APPS_LIST.add("com.pure.indosat.care");
        INDOSAT_APPS_LIST.add("com.indosatapps.dompetku");
        INDOSAT_APPS_LIST.add("com.spotify.music");
        INDOSAT_APPS_LIST.add("com.granita.contacticloudsync");
        INDOSAT_APPS_LIST.add("com.indosat.cipika");
        INDOSAT_APPS_LIST.add("com.bbm");
        INDOSAT_APPS_LIST.add("se.feomedia.quizkampen.id.lite");
        INDOSAT_APPS_LIST.add("com.facebook.katana");
    }

    public final static ArrayList<String> INDOSAT_APPS_MARKET_URI;
    static {
        INDOSAT_APPS_MARKET_URI = new ArrayList<String>();
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.pure.indosat.care");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.indosatapps.dompetku");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.spotify.music");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.granita.contacticloudsync");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.indosat.cipika");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.bbm");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=se.feomedia.quizkampen.id.lite");
        INDOSAT_APPS_MARKET_URI.add("market://details?id=com.facebook.katana");
    }

    public final static ArrayList<String> INDOSAT_APPS_PLAY_URI;
    static {
        INDOSAT_APPS_PLAY_URI = new ArrayList<String>();
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.pure.indosat.care&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.indosatapps.dompetku&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.spotify.music&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.granita.contacticloudsync&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.indosat.cipika&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.bbm&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=se.feomedia.quizkampen.id.lite&hl=en");
        INDOSAT_APPS_PLAY_URI.add("https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en");
    }

    public final static ArrayList<Integer> INDOSAT_APPS_IMAGE_INT;
    static {
        INDOSAT_APPS_IMAGE_INT = new ArrayList<Integer>();
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.mycare);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.dompetku);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.spotify);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.icloud);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.cipika_store);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.bbm);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.duel_otak);
        INDOSAT_APPS_IMAGE_INT.add(R.drawable.facebook);
    }

    public final static ArrayList<String> INDOSAT_APPS_IMAGE_URI;
    static {
        INDOSAT_APPS_IMAGE_URI = new ArrayList<String>();
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/ak-pAjK7A-n3_9Zn3l3OFCocT-TTK8unbdegyxeEV4KQoSWw9V3HqlevwDx1C3_Bhw=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/bQVxWIz5JcBUVCJlXkizfb_bBUg0DO5WgmpRhlAXPWWTXFKwNn1tiZrE1o9Fmm2iuQ=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/UrY7BAZ-XfXGpfkeWg0zCCeo-7ras4DCoRalC_WXXWTK9q5b0Iw7B0YQMsVxZaNB7DM=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/xUtBpPANOF3-B7DO6zadze6f-ikRMNStJgXJHW2QFq6H14uHITVCLqt4NTfjkGvCBA=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh5.ggpht.com/IwBWQVbmpCpZkGR7ubGOma9g4B0ByLBZrVsGHAqXxu4-5L1ARen6jtqHZmxFjwC0jw2r=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/Pwib7QDQnb-zK756VWLL5SoMyorKm6nsjImGay3bH4QwYVRjU1G8inXuQWR4r0ZfIxY=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/60fALA2cW3EwltcCEvOrSjG31SkPfOPrkREIsv96godMMlz5RLXJEfrIC-xgrBh1Zlc=w300-rw");
        INDOSAT_APPS_IMAGE_URI.add("https://lh3.googleusercontent.com/ZZPdzvlpK9r_Df9C3M7j1rNRi7hhHRvPhlklJ3lfi5jk86Jd1s0Y5wcQ1QgbVaAP5Q=w300-rw");
    }

}
