package com.pa.door.facepadmegvii.util;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by linyue on 15/12/22.
 */
public class ImageLoaderManager {
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(false)  // default
            .cacheInMemory(true) // default
            .cacheOnDisk(false) // default
            .considerExifParams(false) // default
            .displayer(new CircleBitmapDisplayer()) // default
            .handler(new Handler()) // default
            .build();

    private static DisplayImageOptions circleOptions = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)  // default
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .considerExifParams(false) // default
            .displayer(new CircleBitmapDisplayer()) // default
            .handler(new Handler()) // default
            .build();

    private static DisplayImageOptions FadeInOptions = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)  // default
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .considerExifParams(false) // default
            .displayer(new FadeInBitmapDisplayer(500)) // default
            .handler(new Handler()) // default
            .build();

    private static DisplayImageOptions SimplayOption = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)  // default
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .considerExifParams(false) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .handler(new Handler()) // default
            .build();


    public static void initImageLoader(Context applicationContext) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    applicationContext.getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator());
            ImageLoaderConfiguration config = builder.build();
            ImageLoader.getInstance().init(config);
        }
    }

    public static void loadCircleImage(String path, ImageView target) {
        ImageLoader.getInstance().displayImage(path, target, circleOptions);
    }

    public static void loadFadeImage(String path, ImageView target) {
        ImageLoader.getInstance().displayImage(path, target, FadeInOptions);
    }

    public static void loadSimplay(String path, ImageView target){
        ImageLoader.getInstance().displayImage(path, target, SimplayOption);
    }

}
