package com.visitor.obria.facepad.util;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Shaojie on 2018/5/18.
 */

public class ToastUtil {

    public static void Show(Context context,String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
