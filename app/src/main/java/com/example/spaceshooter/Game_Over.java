package com.example.spaceshooter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Game_Over extends AppCompatActivity {
    TextView holder;
    TextView highestScore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        holder = findViewById(R.id.Holder);
        highestScore = findViewById(R.id.highestScore);
    }
}
