package obria.com.videotest.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URLEncoder;

import obria.com.videotest.MainActivity;
import obria.com.videotest.entity.FaceRecognized;
import obria.com.videotest.listener.IWebSocketListener;

/**
 * Created by ysj on 2018/2/27.
 */

public class WebSocketHelper {

    private Activity activity;
    private String koala;
    private String camera;
    private WebSocketClient client;
    private IWebSocketListener listener;
    private boolean mIsHandClose = false;
    private boolean mIsWork = false;
    private  Handler handler;

    public WebSocketHelper(Context context, String koala, String camera, Handler handler) {
        this.activity = (Activity) context;
        this.koala = koala;
        this.camera = camera;
        this.handler = handler;
        mIsHandClose = false;
        mIsWork = true;
    }

    private void init() {
        String url = getUrl();
        try {
//            url = "ws://192.168.0.7:4649/Echo";
            java.net.URI uri = java.net.URI.create(url);
            client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    String status = "open";
                    handler.sendEmptyMessage(100);
                }

                @Override
                public void onMessage(String json) {
                    if (mIsWork == false) {
                        return;
                    }
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }

                    Gson gson = new Gson();
                    try {
                        final FaceRecognized face = gson.fromJson(json, FaceRecognized.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.OnMessage(face);
                                }
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    if (mIsHandClose) {
                        return;
                    }
                    handler.sendEmptyMessage(101);
                    dispose();
                }

                @Override
                public void onError(Exception e) {
//                    error();
                }
            };
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void error() {
        client = null;
        reOpen();
    }

    private void dispose() {

        client.close();
        client = null;
        reOpen();
    }

    public void HandClose() {
        mIsHandClose = true;
    }

    public void pause() {
        mIsWork = false;
    }

    public void work() {
        mIsWork = true;
    }

    Thread thread;

    private void reOpen() {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    open();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public boolean open() {

        try {
            init();
            client.connect();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void setOnWebsocketMessageListener(IWebSocketListener listener) {
        this.listener = listener;
    }

    private String getUrl() {
        String url = String.format("ws://%s:9000/video", koala);
        String rtsp = String.format(Constrant.RTSP_CAMERA, camera);
        String rtspUrlEncode = Util.toURLEncoded(rtsp);
        String result = url + "?url=" + rtspUrlEncode;
        return result;
    }


}
