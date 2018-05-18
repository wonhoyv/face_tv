package com.visitor.obria.facepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.visitor.obria.facepad.fs.WSHelper;
import com.visitor.obria.facepad.util.SharedPreferencesHelper;
import com.visitor.obria.facepad.util.ToastUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

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

        btnSave = (Button) findViewById(R.id.btnSave);
        btnExit = (Button) findViewById(R.id.btnExit);

        et_koala = (EditText) findViewById(R.id.et_koala);
        et_camera = (EditText) findViewById(R.id.et_camera);

        sp = SharedPreferencesHelper.getInstance(this);
        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, "");
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, "");

        et_koala.setText(koala);
        et_camera.setText(camera);


        btnSave.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {
            String koala = et_koala.getText().toString();
            String camera = et_camera.getText().toString();
            sp.setStringValue(SharedPreferencesHelper.KOALA_IP, koala);
            sp.setStringValue(SharedPreferencesHelper.CAMERA_IP, camera);
            WSHelper ws = new WSHelper(koala, camera);
            boolean open = ws.Open();
            ws.Close();
            if (open) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                ToastUtil.Show(this, "连接失败，请检查网络！");
            }
        }

        if (view.getId() == R.id.btnExit) {
            this.finish();
        }
    }
}
