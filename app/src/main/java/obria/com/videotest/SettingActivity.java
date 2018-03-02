package obria.com.videotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import obria.com.videotest.util.Constrant;
import obria.com.videotest.util.SharedPreferencesHelper;
import obria.com.videotest.util.ToastUtil;

public class SettingActivity extends AppCompatActivity {

    ImageButton imageButton_back;
    EditText editText_welcome;
    EditText edittext_recognizeserver;
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
        edittext_camera = (EditText) findViewById(R.id.edittext_camera);
        button_save = (Button) findViewById(R.id.button_save);

        //返回
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });

        //保存
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String welcome = editText_welcome.getText().toString();
                String server = edittext_recognizeserver.getText().toString();
                String camera = edittext_camera.getText().toString();

                if (TextUtils.isEmpty(welcome)) {
                    ToastUtil.shortShow("请输入标题");
                    return;
                }

                if (TextUtils.isEmpty(server)) {
                    ToastUtil.shortShow("请输入识别服务器");
                    return;
                }

                if (TextUtils.isEmpty(camera)) {
                    ToastUtil.shortShow("请输入摄像机");
                    return;
                }

                spHelper.setStringValue(Constrant.key_welcome, welcome);
                spHelper.setStringValue(Constrant.key_server, server);
                spHelper.setStringValue(Constrant.key_camera, camera);

                ToastUtil.show("保存成功！");
            }
        });
    }

    private void loadData() {

        editText_welcome.setText(spHelper.getStringValue(Constrant.key_welcome, ""));
        edittext_recognizeserver.setText(spHelper.getStringValue(Constrant.key_server, ""));
        edittext_camera.setText(spHelper.getStringValue(Constrant.key_camera, ""));
    }
}
