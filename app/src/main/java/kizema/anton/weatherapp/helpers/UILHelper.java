package kizema.anton.weatherapp.helpers;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import kizema.anton.weatherapp.BuildConfig;
import kizema.anton.weatherapp.R;

public class UILHelper {

    public static DisplayImageOptions defOpts;

    public static void init(Context context) {
        /**
         *  Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
         */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());

        /**
         *  Use 1/8th of the available memory for this memory cache.
         */
        final int memoryCache = maxMemory / 8;
        Log.d("UILHelper", "memoryCache size : " + (float) (memoryCache) / (float) (1024 * 1024) + "MB");

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)

                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 100 MiB

                .memoryCacheSizePercentage(30)
                .memoryCache(new UsingFreqLimitedMemoryCache(memoryCache))

                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple());

        if (BuildConfig.DEBUG) {
            config.writeDebugLogs();
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

        initPrivate();
    }

    private static void initPrivate(){
        defOpts = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }
}
