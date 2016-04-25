package com.evol.appUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import android.app.Application;

public class AppController extends Application {
	
	private static AppController sInstance;
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this ;
		initImageLoader();
	}
	/**
     * Initialize universal image loader
     */
    private void initImageLoader() {
        int threadPoolSizeUIL = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB ? 3
                : 1;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                sInstance).threadPoolSize(threadPoolSizeUIL)
                .diskCacheSize(AppConstants.CACHE_MEMORY)
                .denyCacheImageMultipleSizesInMemory().build();
        ImageLoader.getInstance().init(config);
        L.writeLogs(false);
    }

    
    public static Application getAppInstance(){
    	return sInstance;
    } 
    
}
