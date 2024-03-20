package com.example.spaceshooter;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingMenu extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu);

        Log.i("I hate this","test");
        ImageButton btn_Back = findViewById(R.id.btn_Back);
        Button btn_ResetHighscore = findViewById(R.id.btn_ResetHighscore);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_Sound = findViewById(R.id.sw_Sound);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_ResetHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingMenu.this,"This haven't been implemented yet",Toast.LENGTH_SHORT).show();
            }
        });

        sw_Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingMenu.this,"This haven't been implemented yet",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
