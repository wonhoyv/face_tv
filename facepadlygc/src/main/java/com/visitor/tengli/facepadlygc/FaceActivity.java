package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.visitor.tengli.facepadlygc.fs.IDTypeEnum;
import com.visitor.tengli.facepadlygc.util.ImageLoaderManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceActivity extends BaseActivity {

    CircleImageView iv_face;
    TextView tv_name;
    TextView tv_welcome;

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageLoaderManager.initImageLoader(this);
        init();
        show();
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

    protected int getlayout() {
        return R.layout.activity_face;
    }

    private void show() {
        try {
            Bundle bundle = this.getIntent().getBundleExtra("face");
            String name = bundle.getString("name");
            String message = bundle.getString("message");
            String avatar = bundle.getString("avatar");
            int idtype = bundle.getInt("idtype");
            int status = bundle.getInt("status");
            int delay = bundle.getInt("delay");

            tv_name.setText(name);
            tv_welcome.setText(message);
            tv_welcome.setBackgroundColor(this.getResources().getColor(R.color.color_green));

            if (idtype == IDTypeEnum.Face.ordinal()) {
                ImageLoaderManager.loadSimplay(avatar, iv_face);
            }
            if (idtype == IDTypeEnum.ID.ordinal()) {

                iv_face.setImageResource(R.mipmap.card);
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
    }
}
