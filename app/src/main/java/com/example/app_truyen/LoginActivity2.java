package com.example.app_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        TextView signUpAction = findViewById(R.id.textSignUpAction);
        signUpAction.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity2.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
    }
}
