package obria.com.videotest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;
import obria.com.videotest.entity.FaceRecognized;
import obria.com.videotest.entity.RecognizeState;
import obria.com.videotest.listener.IWebSocketListener;
import obria.com.videotest.util.Constrant;
import obria.com.videotest.util.SharedPreferencesHelper;
import obria.com.videotest.util.ToastUtil;
import obria.com.videotest.util.WebSocketHelper;

public class MainActivityBack extends AppCompatActivity implements View.OnClickListener, IWebSocketListener {


    private final static String TAG = "ysj";

    LinearLayout recognize;
    ImageButton button_setting;
    TextView textView_name;
    SharedPreferencesHelper spHelper;

    String welcome;
    String main_koala;
    String slave_koala;
    String camera;

    final static int POPUP_DELAY = 5000;
    WebSocketHelper wsHelper;

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
    Bitmap bitmapStranger;

    LinearLayout root;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_back);
        root = (LinearLayout) findViewById(R.id.root);
        spHelper = new SharedPreferencesHelper(this);
        if (loadData()) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 100) {
                        OnOpen();
                    }
                    if (message.what == 101) {
                        OnError();
                    }
                    return true;
                }
            });

            initView();
            initPlayer();
            View view = LayoutInflater.from(this).inflate(R.layout.optionmenu, null);
            window = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            window.setTouchable(true);
//            window.setOutsideTouchable(true);
            window.setFocusable(true);
            //解决焦点问题
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
            button_setting = (ImageButton) view.findViewById(R.id.button_setting);
            button_setting.setOnClickListener(this);

            wsHelper = new WebSocketHelper(this, slave_koala, camera, handler);
            boolean open = wsHelper.open();
            wsHelper.setOnWebsocketMessageListener(this);
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
        options.add(":network-caching=300");//网络缓存

        libVLC = new LibVLC(options);
        mediaPlayer = new org.videolan.libvlc.MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new org.videolan.libvlc.MediaPlayer.EventListener() {
            @Override
            public void onEvent(org.videolan.libvlc.MediaPlayer.Event event) {

                switch (event.type) {
                    case MediaPlayer.Event.MediaChanged:
                        Log.d(TAG, "MediaChanged");
                        break;
                    case MediaPlayer.Event.Opening:
                        Log.d(TAG, "Opening");
                        break;
                    case MediaPlayer.Event.Buffering:
                        Log.d(TAG, "Buffering");
                        if (event.getBuffering() > 10) {
                            hideLoading();
                        }
                        break;
                    case MediaPlayer.Event.Playing:
                        Log.d(TAG, "Playing");
                        break;
                    case MediaPlayer.Event.Paused:
                        Log.d(TAG, "Paused");
                        break;
                    case MediaPlayer.Event.Stopped:
                        Log.d(TAG, "Stopped");
                        break;
                    case MediaPlayer.Event.EndReached:
                        Log.d(TAG, "EndReached");
                        break;
                    case MediaPlayer.Event.ESDeleted:
                        Log.d(TAG, "ESDeleted");
                        break;
                    case MediaPlayer.Event.EncounteredError:
                        Log.d(TAG, "EncounteredError");
                        break;
                    case MediaPlayer.Event.TimeChanged:
                        Log.d(TAG, "TimeChanged");
                        break;
                    case MediaPlayer.Event.PositionChanged:
                        Log.d(TAG, "PositionChanged");
                        break;
                    case MediaPlayer.Event.SeekableChanged:
                        Log.d(TAG, "SeekableChanged");
                        break;
                    case MediaPlayer.Event.PausableChanged:
                        Log.d(TAG, "PausableChanged");
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
            }

            @Override
            public void onSurfacesDestroyed(IVLCVout vlcVout) {

            }

            @Override
            public void onHardwareAccelerationError(IVLCVout vlcVout) {

            }
        });
    }

    private boolean loadData() {

        welcome = spHelper.getStringValue(Constrant.key_welcome, "");
        main_koala = spHelper.getStringValue(Constrant.key_main_server, "");
        slave_koala = spHelper.getStringValue(Constrant.key_slave_server, "");
        camera = spHelper.getStringValue(Constrant.key_camera, "");

        if (TextUtils.isEmpty(welcome) || TextUtils.isEmpty(main_koala) || TextUtils.isEmpty(camera)) {
            gotoSettingActivity();
            return false;
        }
        return true;
    }

    private void initView() {

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        textView_title = (TextView) findViewById(R.id.textview_title);

        welcome = welcome.trim();
        StringBuilder sb = new StringBuilder();
//        int len = welcome.trim().length();
//        for (int i = 0; i < len; i++) {
//            String item = welcome.substring(i, i + 1);
//            sb.append(item + " ");
//        }
//        welcome = sb.toString();

        char[] chars = welcome.toCharArray();
        for (char c : chars) {
            int i = (int) c;
            if (c == '2') {
                sb.append(c);
            } else {
                sb.append(c + " ");
            }
        }
        welcome = sb.toString().toString();
        textView_title.setText(welcome);

        circleImageView = (CircleImageView) findViewById(R.id.cirleImageView);
        statusImageView = (ImageView) findViewById(R.id.statusImageView);

        recognize = (LinearLayout) findViewById(R.id.recognize);
        textView_name = (TextView) findViewById(R.id.textview_name);

//        camera = "192.168.0.10";
        String temp = String.format(Constrant.RTSP_CAMERA, camera);
        cameraRtsp_uri = Uri.parse(temp);
    }

    private Handler mDelayHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            Animation animation_tranlsate = AnimationUtils.loadAnimation(MainActivityBack.this, R.anim.translate);
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
    boolean open = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            open = true;
            window.showAtLocation(root, Gravity.BOTTOM, 0, 0);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (window != null && open) {
                open = false;
                window.dismiss();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnOpen() {

        mediaPlayer.play();
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();
    }

    @Override
    public void OnError() {
        ToastUtil.shortShow("网络异常");
        int a = mediaPlayer.getPlayerState();
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    private void showFace(FaceRecognized face) {

        String name = "";
        String avatar = "";
        if (TextUtils.equals(face.type, RecognizeState.recognized.toString())) {
            if (face.person != null) {
                avatar = face.person.avatar;
                if (avatar.startsWith("http") == false) {
                    avatar = "http://" + main_koala + face.person.avatar;
                }
                if (face.person.subject_type == 0) {
                    //业主
                    name = "业主";
                    recognize.setBackground(this.getResources().getDrawable(R.drawable.yzbg));
                    statusImageView.setImageResource(R.mipmap.yz);
                    int yzColor = this.getResources().getColor(R.color.yz);
                    textView_name.setTextColor(yzColor);
                }
                if (face.person.subject_type == 1) {
                    //租户
                    name = "租户";
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
        wsHelper.HandClose();
    }
}
