package com.visitor.tengli.facepadlygc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.visitor.tengli.facepadlygc.util.DeviceUtil;
/**
 * Created by Shaojie on 2018/5/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayout());
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        DeviceUtil.hideBottomUIMenu(this);
        myCreate();
    }

    protected abstract int getlayout();

    protected abstract void myCreate();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
