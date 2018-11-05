package com.visitor.tengli.facepadlygc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * created by yangshaojie  on 2018/11/5
 * email: ysjr-2002@163.com
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    CrashHandler(Context context){
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (e != null) {
            e.printStackTrace();
        }

        Intent intent = new Intent(mContext.getApplicationContext(), InitActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

        System.exit(2);

    }
}
