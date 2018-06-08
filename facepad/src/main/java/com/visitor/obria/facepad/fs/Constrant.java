package com.visitor.obria.facepad.fs;

import com.visitor.obria.facepad.util.Util;

/**
 * Created by ysj on 2018/2/27.
 */

public class Constrant {

    public static String RTSP_CAMERA = "rtsp://%s/user=admin&password=&channel=1&stream=0.sdp?";

    public static String key_welcome = "welcome";
    public static String key_main_server = "server";
    public static String key_slave_server = "slaveserver";
    public static String key_camera = "camera";

    public static String getUrl(String koala, String camera) {
        String url = String.format("ws://%s:9000/video", koala);
        String rtsp = String.format(Constrant.RTSP_CAMERA, camera);
        String rtspUrlEncode = Util.toURLEncoded(rtsp);
        String result = url + "?url=" + rtspUrlEncode;
        return result;
    }
}
