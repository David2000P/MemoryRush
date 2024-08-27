package com.example.memoryrush;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class GameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private GridView gridView;
    private boolean isGameActive;

    private int level = 1;
    private int score = 0;
    private int lives = 3;
    private int consecutiveMismatches = 0;
    private TextView scoreText, levelText, timerText, livesText, warningText;
    private CountDownTimer countDownTimer;
    private long startTime;
    private long totalTime;
    private final Handler handler = new Handler();
    private boolean gameOverDueToTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        gridView = findViewById(R.id.gridView);
        scoreText = findViewById(R.id.scoreText);
        levelText = findViewById(R.id.levelText);
        timerText = findViewById(R.id.timerText);
        livesText = findViewById(R.id.livesText);
        warningText = findViewById(R.id.warningText);

        isGameActive = true;

        startTime = System.currentTimeMillis();
        setupGame();
    }

    @SuppressLint("SetTextI18n")
    private void setupGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
//github hochladen versuch
        consecutiveMismatches = 0;

        levelText.setText("Level " + level);
        scoreText.setText("Score: " + score);
        livesText.setText("❤️ " + lives);
        warningText.setVisibility(View.GONE);

        if (level % 5 == 0 && level != 0) {
            lives += 3;
            livesText.setText("❤️ " + lives);
            Toast.makeText(this, "3 Leben hinzugefügt!", Toast.LENGTH_SHORT).show();
        }

        long timerDuration = getTimerDurationForLevel(level);
        startTimer(timerDuration);

        int pairCount = calculatePairCount(level);
        MemoryGridAdapter adapter = new MemoryGridAdapter(this, pairCount, isGameActive, level);
        gridView.setAdapter(adapter);

        adapter.setOnPairsMatchListener(new MemoryGridAdapter.OnPairsMatchListener() {
            @Override
            public void onAllPairsMatched() {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (level < 99) {
                    level++;
                    setupGame();
                } else {
                    Toast.makeText(GameActivity.this, "You've reached the maximum level.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPairMatched(int streak) {
                score += 10 * streak;
                scoreText.setText("Score: " + score);
                consecutiveMismatches = 0;
            }

            @Override
            public void onPairMismatched() {
                consecutiveMismatches++;
                score -= 5;
                scoreText.setText("Score: " + score);
                if (consecutiveMismatches == 2) {
                    lives--;
                    livesText.setText("❤️ " + lives);

                    warningText.setVisibility(View.VISIBLE);
                    warningText.setText("2 Paare hintereinander falsch!!!");

                    handler.postDelayed(() -> warningText.setVisibility(View.GONE), 1500);

                    consecutiveMismatches = 0;
                    if (lives <= 0) {
                        isGameActive = false;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        gameOverDueToTime = false;
                        totalTime = (System.currentTimeMillis() - startTime) / 1000; // Gesamtzeit in Sekunden berechnen
                        showGameOverDialog();
                    }
                }
            }


            @Override
            public void onHeartPairMatched() {
                lives += 3;
                livesText.setText("❤️ " + lives);
                Toast.makeText(GameActivity.this, "Herzpaar gefunden! 3 Leben hinzugefügt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getTimerDurationForLevel(int level) {
        if (level <= 2) {
            return 10000;
        } else if (level <= 4) {
            return 15000;
        } else if (level == 5) {
            return 30000;
        } else if (level <= 8) {
            return 40000;
        } else {
            return 80000;
        }
    }

    private void startTimer(long time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time: " + millisUntilFinished / 1000);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timerText.setText("Time's up!");
                if (isGameActive) {
                    isGameActive = false;
                    gameOverDueToTime = true;
                    totalTime = (System.currentTimeMillis() - startTime) / 1000;
                    showGameOverDialog();
                }
            }
        }.start();
    }




    private void saveHighscore(int level, long totalTime, int score) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userName = currentUser != null ? currentUser.getDisplayName() : "Unknown";

        DatabaseReference highscoreRef = database.getReference("highscores");

        String key = highscoreRef.push().getKey();
        if (key != null) {
            Highscore highscore = new Highscore(userName, level, totalTime, score);
            highscoreRef.child(key).setValue(highscore);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Spiel beenden")
                .setMessage("Möchtest du das Spiel wirklich beenden?")
                .setPositiveButton("Ja", (dialog, which) -> {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    isGameActive = false;
                    super.onBackPressed();
                })
                .setNegativeButton("Nein", null)
                .show();
    }





    private void showGameOverDialog() {
        String formattedTime = formatTime(totalTime);
        String gameOverMessage = gameOverDueToTime ? "Achte auf die Zeit!" : "Keine Leben mehr!";

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("☹️ Spiel verloren");
        builder.setMessage(gameOverMessage + "\nErzielter Score: " + score + "\nGesamtzeit: " + formattedTime);
        builder.setCancelable(false);
        builder.setPositiveButton("Hauptmenü", (dialog, which) -> {
            saveHighscore(level, totalTime, score);
            finish();
        });
        builder.setNegativeButton("Neustart", (dialog, which) -> {
            level = 1;
            score = 0;
            lives = 5;
            consecutiveMismatches = 0;
            isGameActive = true;
            startTime = System.currentTimeMillis();
            setupGame();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + " Min " + seconds + " Sek";
    }

    private int calculatePairCount(int level) {
        int basePairs = 2;
        return basePairs + level - 1;
    }
}
