package com.visitor.tengli.facepadlygc;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yangshaojie  on 2018/11/5
 * email: ysjr-2002@163.com
 */
public class MyApp extends Application {

    private static MyApp instance;
    private SharedPreferencesHelper mHelper;
    private final List<Activity> allActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mHelper = SharedPreferencesHelper.getInstance(this.getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                allActivities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

                allActivities.remove(activity);
            }
        });
    }

    public static MyApp getInstance() {
        return instance;
    }

    public SharedPreferencesHelper getHelper() {
        return mHelper;
    }

    public void exitApp() {

        finishAllActivity();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void finishAllActivity() {
        synchronized (allActivities) {
            for (Activity act : allActivities) {
                act.finish();
            }
            allActivities.clear();
        }
    }
}
