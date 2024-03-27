package com.example.spaceshooter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.contentcapture.DataShareWriteAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    Intent backGroundMusic;
    Intent lowBatteryNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backGroundMusic = new Intent(this, BackgroundMusicService.class);
        lowBatteryNoti = new Intent(this, LowBatteryNotification.class);
        startService(backGroundMusic);
        startService(lowBatteryNoti);
        setContentView(R.layout.main_menu);

        findViewById(R.id.btn_Play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog(Gravity.NO_GRAVITY);
            }
        });

        findViewById(R.id.btn_QUIT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(backGroundMusic);
                stopService(lowBatteryNoti);
                finish();
            }
        });
        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
    }

    private void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.btn_menu));
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.btn_setting) {
                    startActivity(new Intent(MainMenu.this, SettingMenu.class));
                }
                return false;
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    private void initDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = gravity;
        window.setAttributes(windowAtributes);
        EditText nameEdt = dialog.findViewById(R.id.dialog_nameTyper);
        Button notInterested = dialog.findViewById(R.id.dialog_NotInterested);
        Button play = dialog.findViewById(R.id.dialog_Play);
        notInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, GameActivity.class));
            }
        });
        notInterested.setOnClickListener(v -> {
            dialog.dismiss();
        });

        play.setOnClickListener(v -> {
            String nameSave = nameEdt.getText().toString();
            if(nameSave.compareTo("") == 0) {
                nameSave = "Annoy";
            }
            Intent intent = new Intent(MainMenu.this, GameActivity.class);
            intent.putExtra("name", nameSave);
            startActivity(intent);
        });
        dialog.show();

    }
}
