package com.pa.door.facepadex.fs;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by Shaojie on 2018/5/18.
 */

public class WSHelper {

    private String koala;
    private String camera;
    private WebSocketClient client;

    public WSHelper(String koala, String camera) {
        this.koala = koala;
        this.camera = camera;
    }

    public void init() {
        String url = Constrant.getUrl(koala, camera);
        java.net.URI uri = java.net.URI.create(url);
        client = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {

                open = true;
                are.set();
            }

            @Override
            public void onMessage(String s) {

            }

            @Override
            public void onClose(int i, String s, boolean b) {

                open = false;
                are.set();
            }

            @Override
            public void onError(Exception e) {

                open = false;
                are.set();
            }
        };
    }

    final AutoResetEvent are = new AutoResetEvent(false);
    boolean open = false;
    public boolean Open() {

        try {
            init();
            client.connect();
            are.waitOne();
            return open;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ysj",e.getMessage());
            String error = e.getMessage();
            return false;
        }
    }

    public void Close() {
        client.close();
    }
}
