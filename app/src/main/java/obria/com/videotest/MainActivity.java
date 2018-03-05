package obria.com.videotest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

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

    ProgressBar progressBar;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    LibVLC libVLC;
    org.videolan.libvlc.MediaPlayer mediaPlayer;
    IVLCVout ivlcVout;
    Uri cameraRtsp_uri;

    TextView textView_title;
    CircleImageView circleImageView;
    ImageView statusImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spHelper = new SharedPreferencesHelper(this);
        if (loadData()) {
            initView();
            initPlayer();
            timer = new Timer();
            timer.schedule(new MyTimerTask(), 0, 1000);
        }
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initPlayer() {

        ArrayList<String> options = new ArrayList<>();
//        options.add(":file-caching=1500");//文件缓存
        options.add(":network-caching=300");//网络缓存

//        options.add(":live-caching=1500");//直播缓存
//        options.add(":sout-mux-caching=1500");//输出缓存
//        options.add(":codec=mediacodec,iomx,all");

        libVLC = new LibVLC(options);
        mediaPlayer = new org.videolan.libvlc.MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new org.videolan.libvlc.MediaPlayer.EventListener() {
            @Override
            public void onEvent(org.videolan.libvlc.MediaPlayer.Event event) {

                switch (event.type) {
                    case org.videolan.libvlc.MediaPlayer.Event.Buffering:
                        if (event.getBuffering() > 10) {
                            hideLoading();
                        }
                        break;
                    case org.videolan.libvlc.MediaPlayer.Event.Opening:
//                        ToastUtil.shortShow("opening");
                        break;
                    case org.videolan.libvlc.MediaPlayer.Event.EncounteredError:
                        hideLoading();
//                        ToastUtil.shortShow("error");
                        break;
                }
            }
        });

        Media media = new Media(libVLC, cameraRtsp_uri);
        mediaPlayer.setMedia(media);

        ivlcVout = mediaPlayer.getVLCVout();
        ivlcVout.setVideoView(surfaceView);
        ivlcVout.attachViews();
        ivlcVout.addCallback(new IVLCVout.Callback() {
            @Override
            public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

            }

            @Override
            public void onSurfacesCreated(IVLCVout vlcVout) {
                int sw = getWindow().getDecorView().getWidth();
                int sh = getWindow().getDecorView().getHeight();

                if (sw * sh == 0) {
                    Log.e(TAG, "Invalid surface size");
                    return;
                }
//                mediaPlayer.getVLCVout().setWindowSize(sw, sh);
//                mediaPlayer.setAspectRatio("16:9");
//                mediaPlayer.setScale(0);
            }

            @Override
            public void onSurfacesDestroyed(IVLCVout vlcVout) {

            }

            @Override
            public void onHardwareAccelerationError(IVLCVout vlcVout) {

            }
        });
        mediaPlayer.play();
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();
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

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        textView_title = (TextView) findViewById(R.id.textview_title);
        textView_title.setText(welcome);

        circleImageView = (CircleImageView) findViewById(R.id.cirleImageView);
        statusImageView = (ImageView) findViewById(R.id.statusImageView);

        textview_weekinfo = (TextView) findViewById(R.id.textview_weekinfo);
        textview_timeinfo = (TextView) findViewById(R.id.textview_timeinfo);
        linearLayout = (LinearLayout) findViewById(R.id.recognize);

        imageView_face = (ImageView) findViewById(R.id.imageview_face);
        button_setting = (ImageButton) findViewById(R.id.button_setting);
        button_setting.setOnClickListener(this);

        textView_name = (TextView) findViewById(R.id.textview_name);
        button_test = (ImageButton) findViewById(R.id.button_test);
        button_test.setOnClickListener(this);

        camera = "192.168.0.10";
        String temp = String.format(Constrant.RTSP_CAMERA, camera);
        cameraRtsp_uri = Uri.parse(temp);


        koala = "192.168.0.53";
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
        wsHelper.work();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        wsHelper.work();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wsHelper.pause();
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
                    avatar = "http://" + koala + face.person.avatar;
                }
                if (face.person.subject_type == 0) {
                    //业主
                    name = "业主";
                    statusImageView.setImageResource(R.mipmap.yz);
                    int yzColor = this.getResources().getColor(R.color.yz);
                    textView_name.setTextColor(yzColor);
                }
                if (face.person.subject_type == 1) {
                    //租户
                    name = "租户";
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
        if (face.type == RecognizeState.unrecognized.toString()) {
            //陌生人
            name = "陌生人";
            int yzColor = this.getResources().getColor(R.color.msr);
            textView_name.setTextColor(yzColor);

            String base64Image = face.data.face.image;
            statusImageView.setImageResource(R.mipmap.msr);
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
//        Picasso.with(this).load(uri_avatar).into(imageView_face);
        Picasso.with(this).load(uri_avatar).into(circleImageView);
        linearLayout.startAnimation(animation);
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
