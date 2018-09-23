package com.pa.door.facepadex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pa.door.facepadex.util.ActivityCollector;

/**
 * Created by Shaojie on 2018/5/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayout());
        ActivityCollector.addActivity(this, getClass());
    }

    protected abstract int getlayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
