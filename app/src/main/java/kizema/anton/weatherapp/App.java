package kizema.anton.weatherapp;

import android.app.Application;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;

import kizema.anton.weatherapp.helpers.LocationHelper;

public class App extends Application {

    public static DisplayImageOptions defOpts;

    @Override public void onCreate() {
        super.onCreate();

        LocationHelper.init(this);

        ActiveAndroid.initialize(getApplicationContext());
        LeakCanary.install(this);

        initUil();
    }

    private void initUil(){
        /**
         *  Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
         */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());

        /**
         *  Use 1/8th of the available memory for this memory cache.
         */
        final int memoryCache = maxMemory / 8;
        Log.d("UILHelper", "memoryCache size : " + (float) (memoryCache) / (float) (1024 * 1024) + "MB");

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)

                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 100 MiB
//                .diskCache(new LimitedAgeDiskCache())

                .memoryCacheSizePercentage(30)
                .memoryCache(new UsingFreqLimitedMemoryCache(memoryCache))

                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .imageDownloader(getImageLoader())
//                .imageDownloader(new HttpClientImageDownloader(App.getContext()))
//                .imageDecoder(new NutraBaseImageDecoder(true))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple());

        if (BuildConfig.DEBUG) {
            config.writeDebugLogs();
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

        defOpts = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }

}
