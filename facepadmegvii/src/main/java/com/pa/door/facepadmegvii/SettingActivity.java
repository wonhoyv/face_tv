package com.pa.door.facepadmegvii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pa.door.facepadmegvii.util.DeviceUtil;
import com.pa.door.facepadmegvii.util.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout root;

    Button btnSave;
    Button btnExit;

    EditText et_building;
    EditText et_welcome;
    EditText et_mainkoala;
    EditText et_koala;
    EditText et_camera;

    SharedPreferencesHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        DeviceUtil.hideBottomUIMenu(this);
        initView();
    }

    private void initView() {

        root = (LinearLayout) findViewById(R.id.root);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnExit = (Button) findViewById(R.id.btnExit);

        et_building = (EditText) findViewById(R.id.et_building);
        et_welcome = (EditText) findViewById(R.id.et_welcome);
        et_mainkoala = (EditText) findViewById(R.id.et_mainkoala);
        et_koala = (EditText) findViewById(R.id.et_koala);
        et_camera = (EditText) findViewById(R.id.et_camera);

        sp = SharedPreferencesHelper.getInstance(this);
        String building = sp.getStringValue(SharedPreferencesHelper.BUILDING, Core.building);
        String welcome = sp.getStringValue(SharedPreferencesHelper.WELCOME, Core.welcome);
        String mainkoala = sp.getStringValue(SharedPreferencesHelper.MAIN_KOALA_IP, Core.cameraip);
        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);

        et_building.setText(building);
        et_welcome.setText(welcome);
        et_mainkoala.setText(mainkoala);
        et_koala.setText(koala);
        et_camera.setText(camera);

        btnSave.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {
            String building = et_building.getText().toString().trim();
            String welcome = et_welcome.getText().toString().trim();
            String mainkoala = et_mainkoala.getText().toString().trim();
            String koala = et_koala.getText().toString().trim();
            String camera = et_camera.getText().toString().trim();

            sp.setStringValue(SharedPreferencesHelper.BUILDING, building);
            sp.setStringValue(SharedPreferencesHelper.WELCOME, welcome);
            sp.setStringValue(SharedPreferencesHelper.MAIN_KOALA_IP, mainkoala);
            sp.setStringValue(SharedPreferencesHelper.KOALA_IP, koala);
            sp.setStringValue(SharedPreferencesHelper.CAMERA_IP, camera);
//            boolean open = IPHelper.startPing(koala);
//            if (open) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
//            } else {
//                ToastUtil.Show(this, "连接失败，请检查网络！");
//            }
        }

        if (view.getId() == R.id.btnExit) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }
    }
}
