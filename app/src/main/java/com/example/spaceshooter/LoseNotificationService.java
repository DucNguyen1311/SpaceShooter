package com.example.spaceshooter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class LoseNotificationService extends Service {
    public LoseNotificationService() {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"You are noob",Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}