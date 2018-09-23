package com.pa.door.facepadex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.pa.door.facepadex.fs.WSHelper;
import com.pa.door.facepadex.util.IPHelper;
import com.pa.door.facepadex.util.SharedPreferencesHelper;


public class InitActivity extends AppCompatActivity {

    SharedPreferencesHelper sp;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        sp = SharedPreferencesHelper.getInstance(this);

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    goToMainActivity();
                }
                if (msg.what == 0) {
                    goToSettingActivity();
                }
            }
        };
        initView();
    }

    private void initView() {

        final String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        final String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);
        if (TextUtils.isEmpty(koala) || TextUtils.isEmpty(camera)) {
            goToSettingActivity();
        } else {
            new Thread(new Runnable() {
                boolean brun = true;
                boolean open = false;
                int trycount = 1;

                @Override
                public void run() {
                    while (brun && trycount <= 10) {
                        open = IPHelper.startPing(koala);
                        if (open) {
                            break;
                        }
                        try {
                            trycount++;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendEmptyMessage(open ? 1 : 0);
                }
            }).start();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        this.finish();
    }

    private void goToSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        this.finish();
    }
}
