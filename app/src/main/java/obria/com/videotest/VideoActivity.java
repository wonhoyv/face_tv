package obria.com.videotest;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.media.VideoView;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "ysj";
    String url_1 = "rtsp://admin:harzone123!@192.168.2.70:554/h264/ch1/main/av_stream";
    String url_2 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    String url_3 = "rtsp://192.168.0.10/user=admin&password=&channel=1&stream=0.sdp?";
    //http://blog.csdn.net/lp8800/article/details/62221092
    final Uri test_uri = Uri.parse(url_3);

    android.widget.VideoView myok;
    VideoView mVideoView;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    LibVLC libVLC;
    MediaPlayer mediaPlayer;
    IVLCVout ivlcVout;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

//        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
//        surfaceHolder = surfaceView.getHolder();
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        initPlayer();

//        mVideoView = (VideoView) findViewById(R.id.video_view);
//        mVideoView.setVideoURI(Uri.parse(url_2));
//        mVideoView.start();

//        myok = (android.widget.VideoView) findViewById(R.id.mytest);
//        myok.setVideoURI(test_uri);
//        myok.start();
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initPlayer() {

        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(options);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {

                switch (event.type) {
                    case MediaPlayer.Event.Buffering:
                        showToast("buffering" + String.valueOf(event.getBuffering()));
                        if (event.getBuffering() > 10) {
                            hideLoading();
                        }
                        break;
                    case MediaPlayer.Event.Opening:
                        showToast("opening");
                        break;
                }
            }
        });

        Media media = new Media(libVLC, test_uri);
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

                mediaPlayer.getVLCVout().setWindowSize(sw, sh);
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
    }
}
