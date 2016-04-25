package com.evol.appUtils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.evol.homeLauncher.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class AppUtils {

	public static boolean isAppInstalled(Context context, String packageName) {
		Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (mIntent != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public static DisplayImageOptions getDefaultDisplayOptionsActive() {

		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true)
				.imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.ic_launcher)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();

	}
}
