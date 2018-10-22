package com.pa.door.facepadmegvii.util;

import java.net.URLEncoder;

/**
 * Created by ysj on 2018/3/7.
 */

public final class Util {

    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }
}
