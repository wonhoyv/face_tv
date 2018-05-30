package com.visitor.obria.facepad;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.visitor.obria.facepad.service.FSService;

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceActivity extends AppCompatActivity {

    Handler delayHandler;
    CircleImageView iv_face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        init();
        show();
    }

    protected int getlayout() {
        return R.layout.activity_face;
    }

    private void show() {
        Bundle bundle = this.getIntent().getBundleExtra("face");
        String avatar = bundle.getString("avatar");
        Picasso.with(this).load(avatar).into(iv_face);
    }

    private void init() {

        iv_face = (CircleImageView) findViewById(R.id.iv_face);

        delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(FaceActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        }, 3 * 1000);
    }

}
