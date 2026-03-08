package com.example.app_truyen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView backToLogin = findViewById(R.id.textBackToLoginAction);
        backToLogin.setOnClickListener(v -> finish());
    }
}
