package com.visitor.tengli.facepadlygc;

import android.app.Application;

import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

/**
 * created by yangshaojie  on 2018/11/5
 * email: ysjr-2002@163.com
 */
public class MyApp extends Application {

    private static MyApp instance;
    private SharedPreferencesHelper mHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    public static MyApp getInstance() {
        return instance;
    }

    public SharedPreferencesHelper getHelper() {
        return mHelper;
    }
}
