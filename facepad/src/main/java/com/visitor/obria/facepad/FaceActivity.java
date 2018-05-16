package com.visitor.obria.facepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.visitor.obria.facepad.service.FSService;

public class FaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);


        Intent intent = new Intent(this, FSService.class);
        startService(intent);
    }
}
