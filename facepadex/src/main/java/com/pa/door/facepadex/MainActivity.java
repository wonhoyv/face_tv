package com.pa.door.facepadex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pa.door.facepadex.fs.WebSocketHelper;
import com.pa.door.facepadex.util.ActivityCollector;
import com.pa.door.facepadex.util.DateUtil;
import com.pa.door.facepadex.util.ImageLoaderManager;
import com.pa.door.facepadex.util.SharedPreferencesHelper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intentMyService;
    Timer timer;
    TextView tv_time;
    TextView tv_week;
    WebSocketHelper ws;
    SharedPreferencesHelper sp;
    RelativeLayout rl_root;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMyService();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageLoaderManager.initImageLoader(this);
    }

    private void startMyService() {

        sp = SharedPreferencesHelper.getInstance(this);
        tv_time = (TextView) findViewById(R.id.tv_time);
//        tv_week = (TextView) findViewById(R.id.tv_week);

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        imageView = (ImageView) findViewById(R.id.iv_password);
        rl_root.setOnClickListener(this);
        imageView.setOnClickListener(this);

        timer = new Timer();
        timer.schedule(timerTask, 0, 30 * 1000);
        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);
        ws = new WebSocketHelper(this, koala, camera, handler);
        ws.open();

        dpi();
    }

    private void dpi() {

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        String test = "";
    }

    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            if (mPause) {
                return true;
            }

            if (message.what == 100) {

                if (ActivityCollector.isActivityExist(FaceActivity.class)) {
                    Log.d("ysj", "showing");

                    FaceActivity test = (FaceActivity) ActivityCollector.getActivity(FaceActivity.class);
                    if (test != null) {
                        test.Update();
                    }
                    return true;
                }

                Intent intent = new Intent(getBaseContext(), FaceActivity.class);
                intent.putExtra("face", message.getData());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
            if (message.what == 101) {
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            }
            if (message.what == 102) {
                Toast.makeText(MainActivity.this, "连接关闭", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

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
        stopService(intentMyService);
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
