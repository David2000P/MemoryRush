package com.example.memoryrush;

public class Highscore {
    private String userName;
    private int level;
    private long time;
    private int score;

    public Highscore() {
    }

    public Highscore(String userName, int level, long time, int score) {
        this.userName = userName;
        this.level = level;
        this.time = time;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
