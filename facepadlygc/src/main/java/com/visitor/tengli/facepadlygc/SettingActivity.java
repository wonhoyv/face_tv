package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    Button btnSave;
    Button btnExit;
    ImageView iv_back;
    EditText et_welcome;

    @Override
    protected int getlayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void myCreate() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {

        iv_back = findViewById(R.id.iv_back);
        et_welcome = (EditText) findViewById(R.id.et_welcome);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnExit = findViewById(R.id.btnExit);

        String welcome = MyApp.getInstance().getHelper().getStringValue(SharedPreferencesHelper.WELCOME, Core.welcome);
        et_welcome.setText(welcome);

        iv_back.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {
            String welcome = et_welcome.getText().toString().trim();
            MyApp.getInstance().getHelper().setStringValue(SharedPreferencesHelper.WELCOME, welcome);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }
        if (view.getId() == R.id.btnExit) {

            MyApp.getInstance().exitApp();
        }
        if (view.getId() == R.id.iv_back) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }
    }
}
