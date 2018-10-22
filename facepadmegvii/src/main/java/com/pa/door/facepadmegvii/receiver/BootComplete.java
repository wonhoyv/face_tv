package com.pa.door.facepadmegvii.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pa.door.facepadmegvii.InitActivity;
import com.pa.door.facepadmegvii.InitActivity;

/**
 * Created by yangshaojie on 2018/5/16.
 */

public class BootComplete extends BroadcastReceiver {

    static final String action_boot ="android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(action_boot)){
            Intent mBootIntent = new Intent(context, InitActivity.class);
            mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        }
    }
}
