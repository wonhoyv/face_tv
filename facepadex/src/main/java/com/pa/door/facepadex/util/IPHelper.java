package com.pa.door.facepadex.util;

import android.util.Log;

import java.io.IOException;

/**
 * created by yangshaojie  on 2018/8/27
 * email: ysjr-2002@163.com
 */
public class IPHelper {

    public static boolean startPing(String ip) {
        boolean isexist = false;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec("ping -c 1 -i 0.5 -W 1 " + ip);
            int status = process.waitFor();
            if (status == 0) {
                isexist = true;
            } else {
                isexist = false;
            }
        } catch (IOException e) {
            isexist = false;
        } catch (InterruptedException e) {
            isexist = false;
        } finally {
            process.destroy();
        }
        return isexist;
    }
}
