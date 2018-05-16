package com.visitor.obria.facepad.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.visitor.obria.facepad.FaceActivity;
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
    public int onStartCommand(Intent intent, int flags, int startId) {

        ws = new WebSocketHelper(null, "", "", handler);
        ws.open();

        return super.onStartCommand(intent, flags, startId);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 100) {

                Intent intent = new Intent(getBaseContext(), FaceActivity.class);
                intent.putExtra("face", message.getData());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            return false;
        }
    });

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
