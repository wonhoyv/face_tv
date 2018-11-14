package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.visitor.tengli.facepadlygc.fs.IDTypeEnum;
import com.visitor.tengli.facepadlygc.fs.SocketMessageBean;
import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceActivity extends BaseActivity {

    CircleImageView iv_face;
    TextView tv_name;
    TextView tv_welcome;

    protected int getlayout() {
        return R.layout.activity_face;
    }

    @Override
    protected void myCreate() {
        Log.d("ysj", "faceactivity create");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        DeviceUtil.hideBottomUIMenu(this);
        ImageLoaderManager.initImageLoader(this);
        init();
        showMessage(this.getIntent().getBundleExtra("face"));
    }

    private Handler delayHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            Intent intent = new Intent(FaceActivity.this, MainActivity.class);
            startActivity(intent);
            FaceActivity.this.finish();
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            return false;
        }
    });

    public void Update(int delay) {
        delayHandler.removeMessages(0);
        delayHandler.sendEmptyMessageDelayed(0, delay);
    }

    private void showMessage(Bundle bundle) {
        try {
            String name = bundle.getString("name");
            String message = bundle.getString("message");
            String avatar = bundle.getString("avatar");
            int idtype = bundle.getInt("idtype");
            int status = bundle.getInt("status");
            int delay = bundle.getInt("delay");

            if (status == 0) {
                tv_name.setText(name);
                tv_welcome.setText(message);
                tv_welcome.setBackgroundColor(this.getResources().getColor(R.color.color_green));
                if (idtype == IDTypeEnum.Face.ordinal()) {
                    //人脸
                    ImageLoaderManager.loadSimplay(avatar, iv_face);
                }
                if (idtype == IDTypeEnum.BarCode.ordinal()) {
                    //二维码
                    iv_face.setImageResource(R.mipmap.yes);
                }
                if (idtype == IDTypeEnum.ID.ordinal()) {
                    //身份证
                    iv_face.setImageResource(R.mipmap.yes);
                }
            } else {
                //1 验证失败
                iv_face.setImageResource(R.mipmap.no);
                tv_welcome.setText(message);
            }

            Update(delay);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        iv_face = (CircleImageView) findViewById(R.id.iv_face);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ysj", "faceactivity onDestroy");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(SocketMessageBean bean) {

        Bundle bundle = new Bundle();
        bundle.putString("name", bean.getName());
        bundle.putString("message", bean.getMessage());
        bundle.putInt("idtype", bean.getIDType());
        bundle.putInt("status", bean.getStatus());
        bundle.putString("avatar", bean.getAvatar());
        bundle.putInt("delay", bean.getDelay());

        Log.d("ysj", "face receive message");
        showMessage(bundle);
    }
}
