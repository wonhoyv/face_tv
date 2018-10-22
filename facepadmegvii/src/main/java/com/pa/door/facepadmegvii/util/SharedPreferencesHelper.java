package com.pa.door.facepadmegvii.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ysj on 2018/2/27.
 */

public class SharedPreferencesHelper {

    private SharedPreferences mPref;
    private Context context;

    public static final String KOALA_IP = "koala";
    public static final String  CAMERA_IP = "camera";
    public static final String WELCOME = "welcome";

    private SharedPreferencesHelper(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences("dalu", Context.MODE_PRIVATE);
    }

    private  static  SharedPreferencesHelper helper;
    public static SharedPreferencesHelper getInstance(Context context) {

        if (helper == null) {
            helper = new SharedPreferencesHelper(context);
        }
        return  helper;
    }

    public void setStringValue(String tag, String value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(tag, value);
        editor.commit();
    }

    public String getStringValue(String tag, String defaultValue) {
        return mPref.getString(tag, defaultValue);
    }


}
