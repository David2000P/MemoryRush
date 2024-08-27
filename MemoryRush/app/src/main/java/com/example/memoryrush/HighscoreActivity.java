package com.example.memoryrush;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private HighscoreAdapter adapter;
    private List<Highscore> highscoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHighscores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        highscoreList = new ArrayList<>();
        adapter = new HighscoreAdapter(highscoreList);
        recyclerView.setAdapter(adapter);

        loadHighscores();
    }

    private void loadHighscores() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference highscoreRef = database.getReference("highscores");

        highscoreRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                highscoreList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Highscore highscore = snapshot.getValue(Highscore.class);
                    if (highscore != null) {
                        highscoreList.add(highscore);
                    }
                }

                Collections.sort(highscoreList, (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));


                if (highscoreList.size() > 10) {
                    highscoreList = highscoreList.subList(0, 10);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
