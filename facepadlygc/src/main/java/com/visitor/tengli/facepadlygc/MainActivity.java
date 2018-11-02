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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visitor.tengli.facepadlygc.fs.SocketMessageBean;
import com.visitor.tengli.facepadlygc.service.UdpService;
import com.visitor.tengli.facepadlygc.util.ActivityCollector;
import com.visitor.tengli.facepadlygc.util.DateUtil;
import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.IPHelper;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intentMyService;
    Timer timer;
    TextView tv_time;
    TextView tv_week;
    TextView tv_factory;
    TextView tv_welcome;
    SharedPreferencesHelper sp;
    RelativeLayout rl_root;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMyService();
        EventBus.getDefault().register(this);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(SocketMessageBean bean) {
        String data = "";
    }

    private void startMyService() {

        sp = SharedPreferencesHelper.getInstance(this);
        tv_time = (TextView) findViewById(R.id.tv_time);

        tv_factory = (TextView) findViewById(R.id.tv_factory);
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        imageView = (ImageView) findViewById(R.id.iv_password);
        rl_root.setOnClickListener(this);
        imageView.setOnClickListener(this);

        timer = new Timer();
        timer.schedule(timerTask, 0, 30 * 1000);
        String welcome = sp.getStringValue(SharedPreferencesHelper.WELCOME, Core.welcome);

        tv_factory.setText(welcome);

        intentMyService = new Intent(this, UdpService.class);
        startService(intentMyService);

        dpi();

        String ip1 = IPHelper.getIP(this.getApplicationContext());
        String ip2 = IPHelper.getIP(this.getApplication().getApplicationContext());
        String ip3 = IPHelper.getIP(this);
        String ip4 = IPHelper.getIP(this.getApplicationContext());
    }

    private void dpi() {

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        String test = "";
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
        Log.d("ysj", "main onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPause = false;
        Log.d("ysj", "resume");
    }

    boolean mPause = false;

    @Override
    protected void onPause() {
        Log.d("ysj", "main pause");
        mPause = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        stopService(intentMyService);
        if(EventBus.getDefault().isRegistered(this)) {
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
