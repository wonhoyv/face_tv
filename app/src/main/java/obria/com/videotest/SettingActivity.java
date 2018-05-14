package obria.com.videotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import obria.com.videotest.util.Constrant;
import obria.com.videotest.util.SharedPreferencesHelper;
import obria.com.videotest.util.ToastUtil;

public class SettingActivity extends AppCompatActivity {

    ImageButton imageButton_back;
    EditText editText_welcome;
    EditText edittext_recognizeserver;
    EditText editText_slave_recognizeserver;
    EditText edittext_camera;
    Button button_save;

    SharedPreferencesHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spHelper = new SharedPreferencesHelper(this);
        initView();
        loadData();
    }

    private void initView() {

        imageButton_back = (ImageButton) findViewById(R.id.imageButton_back);
        editText_welcome = (EditText) findViewById(R.id.edittext_welcome);
        edittext_recognizeserver = (EditText) findViewById(R.id.edittext_recognizeserver);
        editText_slave_recognizeserver = (EditText) findViewById(R.id.edittext_slave_recognizeserver);
        edittext_camera = (EditText) findViewById(R.id.edittext_camera);
        button_save = (Button) findViewById(R.id.button_save);

        //返回
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingActivity.this, MainActivityBack.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });

        //保存
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String welcome = editText_welcome.getText().toString();
                String main_server = edittext_recognizeserver.getText().toString();
                String slave_server = editText_slave_recognizeserver.getText().toString();
                String camera = edittext_camera.getText().toString();

                if (TextUtils.isEmpty(welcome)) {
                    ToastUtil.shortShow("请输入标题");
                    return;
                }

                if (TextUtils.isEmpty(main_server)) {
                    ToastUtil.shortShow("请输入主识别服务器");
                    return;
                }

                if (TextUtils.isEmpty(slave_server)) {
                    ToastUtil.shortShow("请输从主识别服务器");
                    return;
                }

                if (TextUtils.isEmpty(camera)) {
                    ToastUtil.shortShow("请输入摄像机");
                    return;
                }

                spHelper.setStringValue(Constrant.key_welcome, welcome);
                spHelper.setStringValue(Constrant.key_main_server, main_server);
                spHelper.setStringValue(Constrant.key_slave_server, slave_server);
                spHelper.setStringValue(Constrant.key_camera, camera);

                ToastUtil.show("保存成功！");
            }
        });
    }

    private void loadData() {

        String a = spHelper.getStringValue(Constrant.key_welcome, "");
        String b = spHelper.getStringValue(Constrant.key_main_server, "");
        String c = spHelper.getStringValue(Constrant.key_slave_server, "");
        String d = spHelper.getStringValue(Constrant.key_camera, "");

        if (TextUtils.isEmpty(a)) {
            a = "您已进入24小时监控区域";
        }
        if (TextUtils.isEmpty(b)) {
            b = "192.168.1.50";
        }
        if (TextUtils.isEmpty(c)) {
            c = "192.168.1.50";
        }
        if (TextUtils.isEmpty(d)) {
            d = "192.168.1.5";
        }

        editText_welcome.setText(a);
        edittext_recognizeserver.setText(b);
        editText_slave_recognizeserver.setText(c);
        edittext_camera.setText(d);
    }
}
