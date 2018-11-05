package com.visitor.tengli.facepadlygc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.IPHelper;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;


public class InitActivity extends AppCompatActivity {

    SharedPreferencesHelper sp;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        sp = SharedPreferencesHelper.getInstance(this);


        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
    }

    private void initView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(InitActivity.this, FaceActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("avatar", "https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=e2a6ce20d9c451daf6f60bed8ec6355b/3b87e950352ac65ce94b300dfff2b21193138a06.jpg");
                bundle.putString("name", "ysj");
                bundle.putString("type", "0");
                intent.putExtra("face", bundle);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        }, 1000);
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
