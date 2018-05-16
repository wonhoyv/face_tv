package com.visitor.obria.facepad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.visitor.obria.facepad.FaceActivity;
import com.visitor.obria.facepad.MainActivity;


/**
 * Created by yangshaojie on 2018/5/16.
 */

public class BootComplete extends BroadcastReceiver {

    static final String action_boot ="android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(action_boot)){
            Intent mBootIntent = new Intent(context, MainActivity.class);
            mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        }
    }
}
