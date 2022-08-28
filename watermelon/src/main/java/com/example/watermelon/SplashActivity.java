package com.example.watermelon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// 스플래시 화면
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.img_splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.img_splash).setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
            }
        });

    }
}