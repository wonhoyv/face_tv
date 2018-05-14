package obria.com.videotest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import android.app.ProgressDialog;

import java.io.IOException;

import obria.com.videotest.util.Constrant;

@SuppressWarnings("")
public class Main2Activity extends AppCompatActivity {

    VideoView vv;
    String camera;
    Uri cameraRtsp_uri;
    private ProgressDialog loadingDialog; // loading
    Button btnPing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView() {

//        loadingDialog = new ProgressDialog(this);
//        loadingDialog.setMessage("Loading...");
//        loadingDialog.show();
//
//        vv = (VideoView) findViewById(R.id.vv);
//        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                loadingDialog.dismiss();
//            }
//        });
//        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                return false;
//            }
//        });
//        camera = "192.168.0.10";
//        String temp = String.format(Constrant.RTSP_CAMERA, camera);
//        cameraRtsp_uri = Uri.parse(temp);
//        vv.setVideoURI(cameraRtsp_uri);
//        vv.requestFocus();
//        vv.start();

        btnPing = (Button) findViewById(R.id.ping);
        btnPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            boolean shit = pingIpAddress("192.168.0.10");
                            if (shit) {
                                Log.d("ysj", "OK");
                            } else {
                                Log.d("ysj", "Error");


                                Log.d("ysj", "shit");
                            }
                            try {
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
        });

    }

    private boolean pingIpAddress(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 5 " + ipAddress);
            int status = process.waitFor();
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
