package com.visitor.obria.facepad;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.visitor.obria.facepad.fs.WebSocketHelper;
import com.visitor.obria.facepad.service.FSService;
import com.visitor.obria.facepad.util.DateUtil;
import com.visitor.obria.facepad.util.SharedPreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

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
    }

    private void startMyService() {

        sp = SharedPreferencesHelper.getInstance(this);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_week = (TextView) findViewById(R.id.tv_week);

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);

        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, "");
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, "");

        ws = new WebSocketHelper(this, koala, camera, handler);
        ws.open();
    }

    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 100) {
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

            tv_time.setText(DateUtil.getTime());
            tv_week.setText(DateUtil.getWeek());
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
