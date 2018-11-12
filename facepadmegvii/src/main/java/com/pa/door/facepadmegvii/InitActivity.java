package com.pa.door.facepadmegvii;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.pa.door.facepadmegvii.util.DeviceUtil;
import com.pa.door.facepadmegvii.util.IPHelper;
import com.pa.door.facepadmegvii.util.SharedPreferencesHelper;


public class InitActivity extends AppCompatActivity {

    SharedPreferencesHelper sp;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        sp = SharedPreferencesHelper.getInstance(this);
        DeviceUtil.hideBottomUIMenu(this);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                goToMainActivity();
            }
        };
        initView();
        dpi();
    }

    //S30 720*1280 2.0
    //5寸 480*800  1.0
    //7寸 600*976  1.0
    //5寸 480*752  1.0
    private void dpi() {

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        float s = dm.scaledDensity;
    }

    private void initView() {

        final String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        final String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);
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
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        this.finish();
    }
}
