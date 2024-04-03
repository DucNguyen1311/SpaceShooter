package com.example.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Game_Over extends AppCompatActivity {
    TextView holder;
    TextView highestScore;
    TextView yourScore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        yourScore = findViewById(R.id.score);
        holder = findViewById(R.id.Holder);
        highestScore = findViewById(R.id.highestScore);
        String tmp = "Your Score : " + getIntent().getIntExtra("playerScore",0);
        yourScore.setText(tmp);
        tmp = "Highest Score : " + getIntent().getIntExtra("highestScore",0);
        highestScore.setText(tmp);
        tmp = "Holder : " + getIntent().getStringExtra("getholder");
        holder.setText(tmp);
        findViewById(R.id.btn_play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Game_Over.this, MainMenu.class));
            }
        });
    }
}
