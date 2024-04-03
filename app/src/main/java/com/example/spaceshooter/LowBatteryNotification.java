package com.example.spaceshooter;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LowBatteryNotification extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        LowBatteryBroadcastReceiver lowBatteryBroadcastReceiver = new LowBatteryBroadcastReceiver();
        registerReceiver(lowBatteryBroadcastReceiver, intentFilter);
    }
}
