package com.example.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent backGroundMusic = new Intent(this, BackgroundMusicService.class);
        Intent lowBatteryNoti = new Intent(this, LowBatteryNotification.class);
        startService(backGroundMusic);
        startService(lowBatteryNoti);
        setContentView(R.layout.main_menu);

        findViewById(R.id.btn_Play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, GameActivity.class));
            }
        });

        findViewById(R.id.btn_Setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("I hate this","test");
                startActivity(new Intent(MainMenu.this, SettingMenu.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
