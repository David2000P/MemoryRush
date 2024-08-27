package com.example.memoryrush;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, LoginActivity.class)));

        registerButton.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, RegisterActivity.class)));
    }
}
