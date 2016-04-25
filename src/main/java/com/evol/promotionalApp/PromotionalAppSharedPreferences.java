package com.evol.promotionalApp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PromotionalAppSharedPreferences {
	
	private static final String APP_PREFS = "APP_PREFS";
	private static PromotionalAppSharedPreferences singlePrefsInstance;
	
	private SharedPreferences appSharedPrefs;
	private SharedPreferences.Editor prefsEditor;

	private String FIRST_RUN = "FIRST_RUN";

	private String INDOSAT_APP_INSTALLED = "INDOSAT_APP_INSTALLED";

	private String APP1_INSTALLED = "APP1_INSTALLED";
	private String APP2_INSTALLED = "APP2_INSTALLED";
	private String APP3_INSTALLED = "APP3_INSTALLED";
	private String APP4_INSTALLED = "APP4_INSTALLED";
	private String APP5_INSTALLED = "APP5_INSTALLED";
	private String APP6_INSTALLED = "APP6_INSTALLED";
	private String APP7_INSTALLED = "APP7_INSTALLED";
	private String APP8_INSTALLED = "APP8_INSTALLED";
	
	
	public PromotionalAppSharedPreferences(Context context){
		this.appSharedPrefs = context.getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
		
	}
	
	public static PromotionalAppSharedPreferences getInstance(Context context) {
		if(singlePrefsInstance == null){
			singlePrefsInstance = new PromotionalAppSharedPreferences(context);
		}
		return singlePrefsInstance;
	}

	public boolean isFirstRun() {
		return appSharedPrefs.getBoolean(FIRST_RUN, true);
	}

	public void setFirstRun(boolean state) {
		prefsEditor.putBoolean(FIRST_RUN, state).commit();
	}
	
	public boolean getAPP1InstalledStatus() {
		return appSharedPrefs.getBoolean(APP1_INSTALLED, false);
	}

	public void setAPP1InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP1_INSTALLED, state).commit();
	}
	
	public boolean getAPP2InstalledStatus() {
		return appSharedPrefs.getBoolean(APP2_INSTALLED, false);
	}

	public void setAPP2InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP2_INSTALLED, state).commit();
	}
	
	public boolean getAPP3InstalledStatus() {
		return appSharedPrefs.getBoolean(APP3_INSTALLED, false);
	}

	public void setAPP3InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP3_INSTALLED, state).commit();
	}
	
	public boolean getAPP4InstalledStatus() {
		return appSharedPrefs.getBoolean(APP4_INSTALLED, false);
	}

	public void setAPP4InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP4_INSTALLED, state).commit();
	}
	
	public boolean getAPP5InstalledStatus() {
		return appSharedPrefs.getBoolean(APP5_INSTALLED, false);
	}

	public void setAPP5InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP5_INSTALLED, state).commit();
	}
	
	public boolean getAPP6InstalledStatus() {
		return appSharedPrefs.getBoolean(APP6_INSTALLED, false);
	}

	public void setAPP6InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP6_INSTALLED, state).commit();
	}
	
	public boolean getAPP7InstalledStatus() {
		return appSharedPrefs.getBoolean(APP7_INSTALLED, false);
	}

	public void setAPP7InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP7_INSTALLED, state).commit();
	}
	
	public boolean getAPP8InstalledStatus() {
		return appSharedPrefs.getBoolean(APP8_INSTALLED, false);
	}

	public void setAPP8InstalledStatus(boolean state) {
		prefsEditor.putBoolean(APP8_INSTALLED, state).commit();
	}

}
