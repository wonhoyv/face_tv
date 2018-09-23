package com.pa.door.facepadex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pa.door.facepadex.fs.WebSocketHelper;
import com.pa.door.facepadex.util.ActivityCollector;
import com.pa.door.facepadex.util.DateUtil;
import com.pa.door.facepadex.util.ImageLoaderManager;
import com.pa.door.facepadex.util.SharedPreferencesHelper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Intent intentMyService;
    Timer timer;
    TextView tv_time;
    TextView tv_week;
    WebSocketHelper ws;
    SharedPreferencesHelper sp;

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
        timer = new Timer();
        timer.schedule(timerTask, 0, 30 * 1000);
        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);
        ws = new WebSocketHelper(this, koala, camera, handler);
        ws.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ysj", "resume");
    }

    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

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
            return false;
        }
    });

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timeHandler.sendEmptyMessage(0);
            Log.d("ysj", "refresh time");
        }
    };

    private android.os.Handler timeHandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            tv_time.setText(DateUtil.getWeek() + " " + DateUtil.getTime());
//            tv_week.setText(DateUtil.getWeek());
            return false;
        }
    });

    @Override
    protected void onStart() {
        Log.d("ysj", "main onstart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("ysj", "main pause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopService(intentMyService);
        super.onDestroy();
    }
}
