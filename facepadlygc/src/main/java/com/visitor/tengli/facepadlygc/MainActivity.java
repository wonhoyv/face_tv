package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visitor.tengli.facepadlygc.fs.SocketMessageBean;
import com.visitor.tengli.facepadlygc.util.DateUtil;
import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.IPHelper;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/*
m 1.0
h 1.5
x 2.0
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Timer timer;
    TextView tv_time;
    TextView tv_building;
    TextView tv_welcome;
    RelativeLayout rl_root;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        EventBus.getDefault().register(this);
        Log.d("ysj", "mainactivity create");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(SocketMessageBean bean) {

        Bundle bundle = new Bundle();
        bundle.putString("name", bean.getName());
        bundle.putString("message", bean.getMessage());
        bundle.putInt("idtype", bean.getIDType());
        bundle.putInt("status", bean.getStatus());
        bundle.putString("avatar", bean.getAvatar());
        bundle.putInt("delay", bean.getDelay());
        Intent intent = new Intent(this, FaceActivity.class);
        intent.putExtra("face", bundle);
        startActivity(intent);
        this.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        Log.d("ysj", "main receive message");
    }

    private void initView() {

        tv_time = findViewById(R.id.tv_time);
        tv_building = findViewById(R.id.tv_building);
        tv_welcome = findViewById(R.id.tv_welcome);
        rl_root = findViewById(R.id.rl_root);
        imageView = findViewById(R.id.iv_password);
        rl_root.setOnClickListener(this);
        imageView.setOnClickListener(this);
//        timer = new Timer();
//        timer.schedule(timerTask, 0, 30 * 1000);

        dpi();

        String ip1 = IPHelper.getIP(this.getApplicationContext());
    }

    //1024 552
    //洛邑古城 600 976
    private void dpi() {

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        float s = dm.scaledDensity;
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_time.setText(DateUtil.getWeek() + " " + DateUtil.getTime());
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        DeviceUtil.hideBottomUIMenu(this);
        Log.d("ysj", "mainactivity onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPause = false;
        String building = MyApp.getInstance().getHelper().getStringValue(SharedPreferencesHelper.BUILDING, Core.building);
        String welcome = MyApp.getInstance().getHelper().getStringValue(SharedPreferencesHelper.WELCOME, Core.welcome);
        tv_building.setText(building);
        tv_welcome.setText(welcome);
        Log.d("ysj", "mainactivity resume");
    }

    boolean mPause = false;

    @Override
    protected void onPause() {
        Log.d("ysj", "mainactivity pause");
        mPause = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("ysj", "mainactivity destroy");
        if (timer != null) {
            timer.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.rl_root) {
            imageView.setVisibility(View.VISIBLE);
            hideHandler.removeMessages(0);
            hideHandler.sendEmptyMessageDelayed(0, 5000);
        }

        if (view.getId() == R.id.iv_password) {
            hideHandler.removeMessages(0);
            imageView.setVisibility(View.GONE);

            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        }
    }

    private Handler hideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            imageView.setVisibility(View.GONE);
        }
    };
}
