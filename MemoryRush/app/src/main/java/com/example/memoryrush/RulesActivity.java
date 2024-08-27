package com.example.memoryrush;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        TextView rulesTextView = findViewById(R.id.rulesTextView);
        rulesTextView.setText(getGameRules());
    }

    private String getGameRules() {
        return "🎮 Memory Rush Game Rules 🎮\n\n" +
                "Welcome to Memory Rush! Are you ready to test your memory and achieve the highest scores? Here’s everything you need to know to succeed:\n\n" +
                "🎯 Objective\n" +
                "- Find Pairs: Your goal is to find all matching pairs of cards on the board. Each card has a matching counterpart – flip them over and remember their positions!\n" +
                "- Level Up: As you find more pairs, you’ll advance to higher levels. The game gets harder with each level!\n\n" +
                "🕹️ How to Play\n" +
                "1. Starting the Game: You start with 5 lives and a limited time that varies depending on your level.\n" +
                "2. Uncovering Pairs: Tap on a card to flip it over. Try to find its matching counterpart!\n" +
                "3. Mismatched Pairs: If you mismatch two pairs consecutively, you lose a life. Be careful and stay focused!\n" +
                "4. Heart Pairs: A special heart pair may appear. Finding it will grant you 3 extra lives.\n" +
                "5. Gaining Lives: Every 5 levels, you receive 3 additional lives as a reward for progressing.\n" +
                "6. Game Over: The game ends when time runs out or you run out of lives. But don’t worry – you can always try again!\n\n" +
                "⏱️ Time Limits\n" +
                "- Levels 1-2: 10 seconds\n" +
                "- Levels 3-4: 15 seconds\n" +
                "- Level 5: 30 seconds\n" +
                "- Levels 6-8: 40 seconds\n" +
                "- From Level 9 onwards: 1 minute\n\n" +
                "🏆 Scoring\n" +
                "- Matching Pairs: You earn points for every pair you find. The faster and more accurately you find pairs, the higher your score.\n" +
                "- Point Deduction: For each mismatched pair, a small number of points will be deducted.\n\n" +
                "📊 Highscores\n" +
                "- Best Performances: Your top scores will be saved in the highscore list, where you can compete against other players.\n" +
                "- Ranking: The highscore list shows the top players based on their level, time, and score. Aim for the top spot!\n\n" +
                "💡 Tips and Tricks\n" +
                "- Improve Memory: Try to remember the positions of the cards, even if you don’t find a pair immediately.\n" +
                "- Stay Focused: Don’t rush – mismatching two pairs in a row will cost you a life!\n" +
                "- Use Heart Pairs: They can save your game, especially in the later levels.\n\n" +
                "🤩 Have Fun!\n" +
                "Enjoy the excitement of Memory Rush and challenge your memory. Good luck and have fun playing!";
    }
}
