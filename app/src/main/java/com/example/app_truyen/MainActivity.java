package com.example.app_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final long DELAY_MS = 3000L;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable openLoginForm = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(MainActivity.this, LoginActivity2.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(openLoginForm, DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openLoginForm);
        super.onDestroy();
    }
}