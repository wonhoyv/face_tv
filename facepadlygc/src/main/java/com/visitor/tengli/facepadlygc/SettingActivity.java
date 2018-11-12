package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.visitor.tengli.facepadlygc.util.DeviceUtil;
import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    ImageView iv_back;
    EditText et_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        DeviceUtil.hideBottomUIMenu(this);
        initView();
    }

    private void initView() {

        iv_back = findViewById(R.id.iv_back);
        et_welcome = (EditText) findViewById(R.id.et_welcome);
        btnSave = (Button) findViewById(R.id.btnSave);

        String welcome = MyApp.getInstance().getHelper().getStringValue(SharedPreferencesHelper.WELCOME, Core.welcome);
        et_welcome.setText(welcome);

        iv_back.setOnClickListener(this);
        btnSave.setOnClickListener(this);
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
        if (view.getId() == R.id.iv_back) {

        }
    }
}
