package obria.com.videotest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "ysj";


    SurfaceView surfaceview;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;

    String url = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    LibVLC libvlc;
    Media media;
    IVLCVout ivlcVout;

    //http://blog.csdn.net/lp8800/article/details/62221092

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
    }

    private void initPlayer() {
        ArrayList<String> options = new ArrayList<>();
        options.add("--aout=opensles");
        options.add("--audio-time-stretch");
        options.add("-vvv");
        libvlc = new LibVLC(VideoActivity.this, options);
        surfaceHolder = surfaceview.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        mediaPlayer = new MediaPlayer(libvlc);
        mediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                switch (event.type) {
                    case MediaPlayer.Event.Buffering:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }

                        if (event.getBuffering() >= 100.0f) {
//                                hideLoading();
//                                Log.i(TAG, "onEvent: buffer success...");
//                                handler.sendEmptyMessageDelayed(CODE_HIDE_BLACK, 500);
//                                handler.sendEmptyMessageDelayed(CODE_GONE_PROGRAMINFO, 5000);
//
//                                handler.sendEmptyMessageDelayed(CODE_HIDE_BLACK, 500);
//                                handler.sendEmptyMessageDelayed(CODE_GONE_PROGRAMINFO, 5000);

                            mediaPlayer.play();
                        } else {
//                                showLoading();
//                                tvCache.setText("缓冲: " + Math.floor(event.getBuffering()) + "%");
                        }

                        break;

                    case MediaPlayer.Event.Playing:
                        Log.i(TAG, "onEvent: playing...");
                        break;

                    case MediaPlayer.Event.EncounteredError:
                        Log.i(TAG, "onEvent: error...");
//                        hideLoading();
                        mediaPlayer.stop();
                        Toast.makeText(VideoActivity.this, "播放出错！", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        Uri uri = Uri.parse("rtsp://admin:harzone123!@192.168.2.70:554/h264/ch1/main/av_stream");
        media = new Media(libvlc, uri);
        mediaPlayer.setMedia(media);

        ivlcVout = mediaPlayer.getVLCVout();
        ivlcVout.setVideoView(surfaceview);
        ivlcVout.attachViews();
        ivlcVout.addCallback(new IVLCVout.Callback() {
            @Override
            public void onSurfacesCreated(IVLCVout vlcVout) {
                int sw = getWindow().getDecorView().getWidth();
                int sh = getWindow().getDecorView().getHeight();

                if (sw * sh == 0) {
                    Log.e(TAG, "Invalid surface size");
                    return;
                }

                mediaPlayer.getVLCVout().setWindowSize(sw, sh);
                mediaPlayer.setAspectRatio("16:9");
                mediaPlayer.setScale(0);
            }

            @Override
            public void onSurfacesDestroyed(IVLCVout vlcVout) {

            }
        });

        mediaPlayer.play();
    }
}
