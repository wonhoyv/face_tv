package com.visitor.obria.facepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.visitor.obria.facepad.fs.WSHelper;
import com.visitor.obria.facepad.util.SharedPreferencesHelper;

public class InitActivity extends AppCompatActivity {

    SharedPreferencesHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        sp = SharedPreferencesHelper.getInstance(this);
        initView();
    }

    private void initView() {

        String koala = sp.getStringValue(SharedPreferencesHelper.KOALA_IP, "");
        String camera = sp.getStringValue(SharedPreferencesHelper.CAMERA_IP, "");

        if (TextUtils.isEmpty(koala) || TextUtils.isEmpty(camera)) {
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
            goToSettingActivity();
            this.finish();
        } else {
            WSHelper ws = new WSHelper(koala, camera);
            boolean open = ws.Open();
            ws.Close();
            if (open) {
                goToMainActivity();

            } else {
                Toast.makeText(this, "bbbb", Toast.LENGTH_SHORT).show();
                goToSettingActivity();
                this.finish();
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    private void goToSettingActivity() {

        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
}
