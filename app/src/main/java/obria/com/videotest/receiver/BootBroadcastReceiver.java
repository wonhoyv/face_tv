package obria.com.videotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import obria.com.videotest.WelcomeActivity;

/**
 * Created by ysj on 2018/2/28.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == ACTION) {
            Intent mainActivityIntent = new Intent(context, WelcomeActivity.class);
            mainActivityIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
