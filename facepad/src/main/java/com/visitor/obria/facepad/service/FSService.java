package com.visitor.obria.facepad.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.visitor.obria.facepad.fs.WebSocketHelper;

/**
 * Created by yangshaojie on 2018/5/16.
 */

public class FSService extends Service {

    WebSocketHelper ws;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        if (ws != null) {
            ws.HandClose();
        }
        super.onDestroy();
    }
}
