package obria.com.videotest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import obria.com.videotest.entity.FaceRecognized;
import obria.com.videotest.entity.RecognizeState;
import obria.com.videotest.listener.IWebSocketListener;
import obria.com.videotest.util.Constrant;
import obria.com.videotest.util.DateUtils;
import obria.com.videotest.util.SharedPreferencesHelper;
import obria.com.videotest.util.ToastUtil;
import obria.com.videotest.util.WebSocketHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWebSocketListener {

    private final static String TAG = "ysj";

    LinearLayout recognize;
    ImageButton button_setting;
    ImageButton button_test;
    TextView textView_name;
    TextView textview_weekinfo;
    TextView textview_timeinfo;
    SharedPreferencesHelper spHelper;

    String welcome;
    String koala;
    String camera;

    final static int POPUP_DELAY = 5000;
    WebSocketHelper wsHelper;

    Timer timer;

    ProgressBar progressBar;

    TextView textView_title;
    CircleImageView circleImageView;
    ImageView statusImageView;
    Bitmap bitmapStranger;

    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = (LinearLayout) findViewById(R.id.root);
        spHelper = new SharedPreferencesHelper(this);
        if (loadData()) {
            initView();
            initPlayer();
//            timer = new Timer();
//            timer.schedule(new MyTimerTask(), 0, 1000);
            View view = LayoutInflater.from(this).inflate(R.layout.optionmenu, null);
            window = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setTouchable(true);
            window.setOutsideTouchable(true);
            window.setContentView(view);
            button_setting = (ImageButton) view.findViewById(R.id.button_setting);
            button_setting.setOnClickListener(this);
        }
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initPlayer() {
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String timeInfo = sdf.format(new Date());
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    textview_weekinfo.setText(DateUtils.getWeekOfDate(new Date()));
                    textview_timeinfo.setText(timeInfo);
                }
            });
        }
    }

    private boolean loadData() {

        welcome = spHelper.getStringValue(Constrant.key_welcome, "");
        koala = spHelper.getStringValue(Constrant.key_server, "");
        camera = spHelper.getStringValue(Constrant.key_camera, "");

        if (TextUtils.isEmpty(welcome) || TextUtils.isEmpty(koala) || TextUtils.isEmpty(camera)) {
            gotoSettingActivity();
            return false;
        }
        return true;
    }

    private void initView() {

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        textView_title = (TextView) findViewById(R.id.textview_title);
        textView_title.setText(welcome);

        circleImageView = (CircleImageView) findViewById(R.id.cirleImageView);
        statusImageView = (ImageView) findViewById(R.id.statusImageView);

        textview_weekinfo = (TextView) findViewById(R.id.textview_weekinfo);
        textview_timeinfo = (TextView) findViewById(R.id.textview_timeinfo);
        recognize = (LinearLayout) findViewById(R.id.recognize);

//        button_setting = (ImageButton) findViewById(R.id.button_setting);
//        button_setting.setOnClickListener(this);

        textView_name = (TextView) findViewById(R.id.textview_name);
        button_test = (ImageButton) findViewById(R.id.button_test);
        button_test.setOnClickListener(this);

//        camera = "192.168.0.10";
//        String temp = String.format(Constrant.RTSP_CAMERA, camera);
//        cameraRtsp_uri = Uri.parse(temp);

//        koala = "192.168.0.53";
        wsHelper = new WebSocketHelper(this, koala, camera);
        boolean open = wsHelper.open();
        if (open) {
            wsHelper.setOnWebsocketMessageListener(this);
        }
    }

    private Handler mDelayHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            Animation animation_tranlsate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
            recognize.startAnimation(animation_tranlsate);
            return true;
        }
    });

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_test) {
            Popup("租户", "http://www.harzone.com/upload/ueditor/image/20180118/6365190024204922824645294.jpg");
        }
        if (view.getId() == R.id.button_setting) {
            gotoSettingActivity();
        }
    }

    private void gotoSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (wsHelper != null)
            wsHelper.work();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (wsHelper != null)
            wsHelper.work();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wsHelper != null)
            wsHelper.pause();
    }

    @Override
    public void OnMessage(FaceRecognized recognize) {

        if (recognize == null) {
            return;
        }
        showFace(recognize);
    }

    PopupWindow window;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            window.showAtLocation(root, Gravity.BOTTOM, 0, 0);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (window != null) {
                window.dismiss();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnError() {

        ToastUtil.shortShow("网络异常");
    }

    private void showFace(FaceRecognized face) {

        String name = "";
        String avatar = "";
        if (TextUtils.equals(face.type, RecognizeState.recognized.toString())) {
            if (face.person != null) {
                avatar = face.person.avatar;
//                koala = "192.168.1.50";
                if (avatar.startsWith("http") == false) {
                    avatar = "http://" + koala + face.person.avatar;
                }
                if (face.person.subject_type == 0) {
                    //业主
                    name = "绿景新洋房欢迎您";
                    recognize.setBackground(this.getResources().getDrawable(R.drawable.yzbg));
                    statusImageView.setImageResource(R.mipmap.yz);
                    int yzColor = this.getResources().getColor(R.color.yz);
                    textView_name.setTextColor(yzColor);
                }
                if (face.person.subject_type == 1) {
                    //租户
                    name = "绿景新洋房欢迎您";
                    recognize.setBackground(this.getResources().getDrawable(R.drawable.zhbg));
                    statusImageView.setImageResource(R.mipmap.zh);
                    int yzColor = this.getResources().getColor(R.color.zh);
                    textView_name.setTextColor(yzColor);
                }
                if (face.person.subject_type == 2) {
                    //VIP
                }
                Popup(name, avatar);
            }
        }
        if (TextUtils.equals(face.type, RecognizeState.unrecognized.toString())) {

            if (bitmapStranger != null && bitmapStranger.isRecycled() == false) {
                bitmapStranger.recycle();
                bitmapStranger = null;
                System.gc();
            }

            //陌生人
            name = "陌生人";
            recognize.setBackground(this.getResources().getDrawable(R.drawable.msrbg));
            int yzColor = this.getResources().getColor(R.color.msr);
            textView_name.setTextColor(yzColor);

            String base64Image = face.data.face.image;
            statusImageView.setImageResource(R.mipmap.msr);
            bitmapStranger = stringToBitmap(base64Image);
            PopupStranger(name, bitmapStranger);
        }
    }

    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void PopupStranger(String name, Bitmap bitmap) {
        recognize.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //发送清屏消息
                mDelayHandler.removeMessages(0);
                mDelayHandler.sendEmptyMessageDelayed(0, POPUP_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView_name.setText(name);
        circleImageView.setImageBitmap(bitmap);
        recognize.startAnimation(animation);
    }

    private void Popup(String name, String avatar) {
        recognize.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //发送清屏消息
                mDelayHandler.removeMessages(0);
                mDelayHandler.sendEmptyMessageDelayed(0, POPUP_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView_name.setText(name);
        Uri uri_avatar = Uri.parse(avatar);
        Picasso.with(this).load(uri_avatar).into(circleImageView);
        recognize.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        timer = null;
        wsHelper.HandClose();
    }
}
