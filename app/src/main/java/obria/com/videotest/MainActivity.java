package obria.com.videotest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import obria.com.videotest.entity.FaceRecognized;
import obria.com.videotest.entity.RecognizeState;
import obria.com.videotest.listener.IWebSocketListener;
import obria.com.videotest.util.Constrant;
import obria.com.videotest.util.DateUtils;
import obria.com.videotest.util.SharedPreferencesHelper;
import obria.com.videotest.util.ToastUtil;
import obria.com.videotest.util.WebSocketHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWebSocketListener {

    LinearLayout linearLayout;
    VideoView videoView;
    ImageView imageView_face;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spHelper = new SharedPreferencesHelper(this);
        if (loadData()) {
            initView();
        }

        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);
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

        textview_weekinfo = (TextView) findViewById(R.id.textview_weekinfo);
        textview_timeinfo = (TextView) findViewById(R.id.textview_timeinfo);
        linearLayout = (LinearLayout) findViewById(R.id.recognize);
        videoView = (VideoView) findViewById(R.id.videoView);

        imageView_face = (ImageView) findViewById(R.id.imageview_face);
        button_setting = (ImageButton) findViewById(R.id.button_setting);
        button_setting.setOnClickListener(this);

        textView_name = (TextView) findViewById(R.id.textview_name);
        button_test = (ImageButton) findViewById(R.id.button_test);
        button_test.setOnClickListener(this);


//        String temp = String.format(Constrant.RTSP_CAMERA, camera);
//        Uri uri = Uri.parse(temp);
        Uri uri = Uri.parse("rtsp://admin:harzone123!@192.168.2.70:554/h264/ch1/main/av_stream");
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                //打开视频失败
                return false;
            }
        });

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
            linearLayout.startAnimation(animation_tranlsate);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void OnMessage(FaceRecognized recognize) {

        if (recognize == null) {
            return;
        }

        showFace(recognize);
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
                if (avatar.startsWith("http") == false) {
                    avatar = "http://" + koala +  face.person.avatar;
                }
                if (face.person.subject_type == 0) {
                    //业主
                    name = "业主";
                }
                if (face.person.subject_type == 1) {
                    //租户
                    name = "租户";
                }
                if (face.person.subject_type == 2) {
                    //VIP
                }
                Popup(name, avatar);
            }
        }
        if (face.type == RecognizeState.unrecognized.toString()) {
            //陌生人
            name = "陌生人";
            String base64Image = face.data.face.image;
            Bitmap bitmap = stringToBitmap(base64Image);
            PopupStranger(name, bitmap);
        }
    }

    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void PopupStranger(String name, Bitmap bitmap) {
        linearLayout.setVisibility(View.VISIBLE);
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
        textView_name.setText(name);
        imageView_face.setImageBitmap(bitmap);
        linearLayout.startAnimation(animation);
    }

    private void Popup(String name, String avatar) {
        linearLayout.setVisibility(View.VISIBLE);
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
        Picasso.with(this).load(uri_avatar).into(imageView_face);
        linearLayout.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        wsHelper.HandClose();
    }
}
