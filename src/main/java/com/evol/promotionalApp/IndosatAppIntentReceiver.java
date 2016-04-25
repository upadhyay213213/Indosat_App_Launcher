package com.evol.promotionalApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.evol.homeLauncher.HomeScreenFragmentTwo;

public class IndosatAppIntentReceiver extends BroadcastReceiver {

	private RemoteViews remoteViews;
	private static AppInstalledListener listener = null;

	public interface AppInstalledListener {
		public void onMessageReceived();
	}

	public static void setListener(AppInstalledListener listener) {
		IndosatAppIntentReceiver.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED") ||
				intent.getAction().equals("android.intent.action.PACKAGE_REMOVED") ||
				intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
			//Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
			if(listener!=null) {
				//Toast.makeText(context, "Received 1", Toast.LENGTH_SHORT).show();
				listener.onMessageReceived();
			}
		}
	}
}
