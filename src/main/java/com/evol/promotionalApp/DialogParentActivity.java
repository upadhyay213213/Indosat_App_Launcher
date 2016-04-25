
package com.evol.promotionalApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.evol.appUtils.AppConstants;
import com.evol.appUtils.AppUtils;
import com.evol.homeLauncher.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DialogParentActivity extends Activity implements OnClickListener{
	
    Button btnsecond_activity;
    Context context;
    
    ImageView app1;
    ImageView app2;
    ImageView app3;
    ImageView app4;
    ImageView app5;
    ImageView app6;
    ImageView app7;
    ImageView app8;
    
    private PromotionalAppSharedPreferences pref;
    
    public static Dialog dialog;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShortcutIcon();
        
        context = this;

        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));//android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.indosat_app_promotional_layout);
        
        app1 = (ImageView) dialog.findViewById(R.id.widget_app1);
        app2 = (ImageView) dialog.findViewById(R.id.widget_app2);
        app3 = (ImageView) dialog.findViewById(R.id.widget_app3);
        app4 = (ImageView) dialog.findViewById(R.id.widget_app4);
        app5 = (ImageView) dialog.findViewById(R.id.widget_app5);
        app6 = (ImageView) dialog.findViewById(R.id.widget_app6);
        app7 = (ImageView) dialog.findViewById(R.id.widget_app7);
        app8 = (ImageView) dialog.findViewById(R.id.widget_app8);
        
        app1.setOnClickListener(this);
        app2.setOnClickListener(this);
        app3.setOnClickListener(this);
        app4.setOnClickListener(this);
        app5.setOnClickListener(this);
        app6.setOnClickListener(this);
        app7.setOnClickListener(this);
        app8.setOnClickListener(this);

		loadImageIcons();

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DialogParentActivity.this.finish();
            }
        });
        updateSharedPreferencesAndWidget();
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

	private void updateSharedPreferencesAndWidget() {

		pref = PromotionalAppSharedPreferences.getInstance(context.getApplicationContext());

		if (AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0))) {
			pref.setAPP1InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0)));
			dialog.findViewById(R.id.widget_app1_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP1InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(0)));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(0),
					(ImageView) dialog.findViewById(R.id.widget_app1_transparent), AppUtils.getDefaultDisplayOptionsActive());
			//mHomeScreenApps.findViewById(R.id.widget_app1_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(0));
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(1))) {
			pref.setAPP2InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(1)));
			dialog.findViewById(R.id.widget_app2_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP2InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(1)));
			// mHomeScreenApps.findViewById(R.id.widget_app2_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(1));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(1),
					(ImageView) dialog.findViewById(R.id.widget_app2_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(2))) {
			pref.setAPP3InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(2)));
			dialog.findViewById(R.id.widget_app3_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP3InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(2)));
			// mHomeScreenApps.findViewById(R.id.widget_app3_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(2));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(2),
					(ImageView) dialog.findViewById(R.id.widget_app3_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(3))) {
			pref.setAPP4InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(3)));
			dialog.findViewById(R.id.widget_app4_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP4InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(3)));
			//mHomeScreenApps.findViewById(R.id.widget_app4_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(3));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(3),
					(ImageView) dialog.findViewById(R.id.widget_app4_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(4))) {
			pref.setAPP5InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(4)));
			dialog.findViewById(R.id.widget_app5_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP5InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(4)));
			//mHomeScreenApps.findViewById(R.id.widget_app5_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(4));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(4),
					(ImageView) dialog.findViewById(R.id.widget_app5_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(5))) {
			pref.setAPP6InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(5)));
			dialog.findViewById(R.id.widget_app6_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP6InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(5)));
			//mHomeScreenApps.findViewById(R.id.widget_app6_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(5));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(5),
					(ImageView) dialog.findViewById(R.id.widget_app6_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(6))) {
			pref.setAPP7InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(6)));
			dialog.findViewById(R.id.widget_app7_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP7InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(6)));
			//mHomeScreenApps.findViewById(R.id.widget_app7_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(6));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(6),
					(ImageView) dialog.findViewById(R.id.widget_app7_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

		if (AppUtils.isAppInstalled(context,AppConstants.INDOSAT_APPS_LIST.get(7))) {
			pref.setAPP8InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(7)));
			dialog.findViewById(R.id.widget_app8_transparent).setBackgroundResource(R.drawable.installed_app);
		} else {
			pref.setAPP8InstalledStatus(AppUtils.isAppInstalled(context, AppConstants.INDOSAT_APPS_LIST.get(7)));
			//mHomeScreenApps.findViewById(R.id.widget_app8_transparent).setBackgroundResource(AppConstants.INDOSAT_APPS_IMAGE_INT.get(7));
			ImageLoader.getInstance().displayImage(AppConstants.INDOSAT_APPS_IMAGE_URI.get(7),
					(ImageView) dialog.findViewById(R.id.widget_app8_transparent), AppUtils.getDefaultDisplayOptionsActive());
		}

	}

    private void ShortcutIcon(){

        Intent shortcutIntent = new Intent(getApplicationContext(), DialogParentActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name).toString());
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    
    @Override
	public void onClick(View v) {
    	    	
    	String uri1 = "";
		String uri2 = "";
		
    	switch(v.getId()) {
    	
    	case R.id.widget_app1 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(0);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(0);
			break;
    	case R.id.widget_app2 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(1);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(1);
			break;
    	case R.id.widget_app3 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(2);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(2);
			break;
    	case R.id.widget_app4 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(3);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(3);
			break;
    	case R.id.widget_app5 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(4);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(4);
			break;
    	case R.id.widget_app6 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(5);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(5);
			break;
    	case R.id.widget_app7 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(6);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(6);
			break;
    	case R.id.widget_app8 :
    		uri1 = AppConstants.INDOSAT_APPS_MARKET_URI.get(7);
			uri2 = AppConstants.INDOSAT_APPS_PLAY_URI.get(7);
    		break;
    	default:
    		break;
    	}
    	
    	if (dialog.isShowing())
    		dialog.dismiss();
        onBackPressed();
        
    	downloadApp(uri1,uri2);
	}
    
    private void downloadApp(String uri1,String uri2) {
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
}