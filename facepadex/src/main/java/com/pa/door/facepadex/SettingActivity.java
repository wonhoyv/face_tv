package com.pa.door.facepadex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pa.door.facepadex.fs.WSHelper;
import com.pa.door.facepadex.util.IPHelper;
import com.pa.door.facepadex.util.SharedPreferencesHelper;
import com.pa.door.facepadex.util.ToastUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout root;

    Button btnSave;
    Button btnExit;

    EditText et_koala;
    EditText et_camera;

    SharedPreferencesHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        root = (LinearLayout) findViewById(R.id.root);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnExit = (Button) findViewById(R.id.btnExit);

        et_koala = (EditText) findViewById(R.id.et_koala);
        et_camera = (EditText) findViewById(R.id.et_camera);

        sp = SharedPreferencesHelper.getInstance(this);
        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, Core.cameraip);
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, Core.camera_rtsp);

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
            String koala = et_koala.getText().toString();
            String camera = et_camera.getText().toString();
            sp.setStringValue(SharedPreferencesHelper.KOALA_IP, koala);
            sp.setStringValue(SharedPreferencesHelper.CAMERA_IP, camera);
            boolean open = IPHelper.startPing(koala);
            if (open) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                this.finish();
            } else {
                ToastUtil.Show(this, "连接失败，请检查网络！");
            }
        }

        if (view.getId() == R.id.btnExit) {
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            this.finish();
        }
    }
}
