package com.example.memoryrush;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {

    private final List<Highscore> highscoreList;

    public HighscoreAdapter(List<Highscore> highscoreList) {
        this.highscoreList = highscoreList;
    }

    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_highscore, parent, false);
        return new HighscoreViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder holder, int position) {
        Highscore highscore = highscoreList.get(position);

        holder.tvRank.setText((position + 1) + ".");
        holder.tvUserName.setText(highscore.getUserName());
        holder.tvLevel.setText("Level: " + highscore.getLevel());
        holder.tvTime.setText("Zeit: " + formatTime(highscore.getTime()));
        holder.tvScore.setText("Score: " + highscore.getScore());
    }

    @Override
    public int getItemCount() {
        return highscoreList.size();
    }

    public static class HighscoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUserName, tvLevel, tvTime, tvScore;

        public HighscoreViewHolder(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }

    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + " Min " + seconds + " Sek";
    }
}
