package com.visitor.tengli.facepadlygc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.visitor.tengli.facepadlygc.service.UdpService;
import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.IPHelper;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;


public class InitActivity extends AppCompatActivity {

    Intent intentMyService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        DeviceUtil.hideBottomUIMenu(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService();
        initView();
    }

    private void startService() {
        intentMyService = new Intent(this, UdpService.class);
        startService(intentMyService);
    }

    private void initView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(InitActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                InitActivity.this.finish();
            }
        }, 1000);
    }
}
