package com.example.memoryrush;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.logoutButton);
        Button playButton = findViewById(R.id.playButton);
        Button highscoreButton = findViewById(R.id.highscoreButton);
        Button rulesButton = findViewById(R.id.rulesButton);

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        highscoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HighscoreActivity.class);
            startActivity(intent);
        });

        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RulesActivity.class);
            startActivity(intent);
        });
    }
}
