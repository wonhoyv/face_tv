package com.visitor.tengli.facepadlygc;

import android.app.Application;

/**
 * created by yangshaojie  on 2018/11/5
 * email: ysjr-2002@163.com
 */
public class LYGCApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }
}
