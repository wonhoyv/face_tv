package com.visitor.tengli.facepadlygc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.visitor.tengli.facepadlygc.util.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout root;

    Button btnSave;
    Button btnExit;

    EditText et_welcome;
    EditText et_mainkoala;
    EditText et_koala;
    EditText et_camera;

    SharedPreferencesHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
    }

    private void initView() {

        root = (LinearLayout) findViewById(R.id.root);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnExit = (Button) findViewById(R.id.btnExit);

        et_welcome = (EditText) findViewById(R.id.et_welcome);

        sp = SharedPreferencesHelper.getInstance(this);
        String welcome = sp.getStringValue(SharedPreferencesHelper.WELCOME, Core.building);
        et_welcome.setText(welcome);

        btnSave.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {
            String welcome = et_welcome.getText().toString().trim();
            sp.setStringValue(SharedPreferencesHelper.WELCOME, welcome);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }

        if (view.getId() == R.id.btnExit) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }
    }
}
