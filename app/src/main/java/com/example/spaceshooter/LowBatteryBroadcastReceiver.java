package com.example.spaceshooter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LowBatteryBroadcastReceiver extends BroadcastReceiver {
    private final static String BATTERY_LEVEL = "level";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Low battery, please charge!", Toast.LENGTH_LONG).show();

    }
}
