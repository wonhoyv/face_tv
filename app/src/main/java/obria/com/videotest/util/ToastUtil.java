package obria.com.videotest.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import obria.com.videotest.R;

/**
 * com.megvii.alibaba.door.presenter
 * ali-door
 * @author rick
 * 2017/9/14 下午7:23
 */

public class ToastUtil {


    private static ToastUtil td;

    public static void show(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (td == null) {
            td = new ToastUtil(MyApplication.getContext());
        }
        td.setText(msg);
        td.create().show();
    }

    public static void shortShow(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (td == null) {
            td = new ToastUtil(MyApplication.getContext());
        }

        td.setText(msg);
        td.createShort().show();
    }


    Context context;
    private Toast toast;
    private String msg;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public Toast create() {
        try {
            View contentView = View.inflate(context, R.layout.dialog_toast, null);
            TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
            if(toast == null){
                toast = new Toast(context);
                toast.setView(contentView);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                tvMsg.setText(msg);
            } else {
                toast.setView(contentView);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                tvMsg.setText(msg);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return toast;
    }

    private Toast createShort() {
        try {
            View contentView = View.inflate(context, R.layout.dialog_toast, null);
            TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
            if(toast == null){
                toast = new Toast(context);
                toast.setView(contentView);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                tvMsg.setText(msg);
            } else {
                toast.setView(contentView);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                tvMsg.setText(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return toast;
    }

    public void show() {
        msgHandler.sendEmptyMessage(0);

        /*if (toast != null) {
            toast.show();
        }*/
    }

    public void setText(String text) {
        msg = text;
    }

    private Handler msgHandler = new Handler(){
        public void handleMessage(Message msg) {
            if (toast != null) {
                toast.show();
            }
        }
    };
}
