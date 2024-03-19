package com.example.spaceshooter;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BackGround {
    int x, y;
    Bitmap background;

    BackGround(int screenX, int screenY, Resources res) {
        background = BitmapFactory.decodeResource(res, R.drawable.space);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }
}
