package com.pa.door.facepadmegvii;

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

import com.pa.door.facepadmegvii.util.ImageLoaderManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceActivity extends BaseActivity {

    CircleImageView iv_face;
    TextView tv_name;
    TextView tv_welcome;

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        show();
        delayHandler.removeMessages(0);
        delayHandler.sendEmptyMessageDelayed(0, 3 * 1000);
    }

    private Handler delayHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            Intent intent = new Intent(FaceActivity.this, MainActivity.class);
            startActivity(intent);
            FaceActivity.this.finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            return false;
        }
    });

    public void Update() {
        delayHandler.removeMessages(0);
        delayHandler.sendEmptyMessageDelayed(0, 3 * 1000);
    }

    protected int getlayout() {
        return R.layout.activity_face;
    }

    private void show() {
        try {
            Bundle bundle = this.getIntent().getBundleExtra("face");
            String type = bundle.getString("type");
            if (TextUtils.equals(type, "0")) {
                String avatar = bundle.getString("avatar");
                String name = bundle.getString("name");
                tv_name.setText(name);
                //Picasso.with(this).load(avatar).into(iv_face);
                ImageLoaderManager.loadSimplay(avatar, iv_face);
                tv_welcome.setText("请通行");
                tv_welcome.setBackgroundColor(this.getResources().getColor(R.color.color_green));
            } else {
                String name = "陌生人";
                tv_name.setText(name);
                String avatar = bundle.getString("avatar");
                Bitmap bitmap = stringToBitmap(avatar);
                iv_face.setImageBitmap(bitmap);
                tv_welcome.setText("禁止进入");
                tv_welcome.setBackgroundColor(this.getResources().getColor(R.color.color_red));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        iv_face = (CircleImageView) findViewById(R.id.iv_face);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
    }

    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ysj", "faceactivity onDestroy");
    }
}
