package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class healthBar {
    private Bitmap btm1, btm2, btm3, btm4, btm5, btm6, btm7, btm8, btm9, btm10;
    private int counter;

    public healthBar(Resources res) {
        btm1 = BitmapFactory.decodeResource(res, R.drawable.health20);
        btm2 = BitmapFactory.decodeResource(res, R.drawable.health18);
        btm3 = BitmapFactory.decodeResource(res, R.drawable.health16);
        btm4 = BitmapFactory.decodeResource(res, R.drawable.health14);
        btm5 = BitmapFactory.decodeResource(res, R.drawable.health12);
        btm6 = BitmapFactory.decodeResource(res, R.drawable.health10);
        btm7 = BitmapFactory.decodeResource(res, R.drawable.health8);
        btm8 = BitmapFactory.decodeResource(res, R.drawable.health6);
        btm9 = BitmapFactory.decodeResource(res, R.drawable.health4);
        btm10 = BitmapFactory.decodeResource(res, R.drawable.health2);
        counter = 1;
    }

    public Bitmap getBitmap() {
        switch (counter) {
            case 1:
                return btm1;
            case 2:
                return btm2;
            case 3:
                return btm3;
            case 4:
                return btm4;
            case 5:
                return btm5;
            case 6:
                return btm6;
            case 7:
                return btm7;
            case 8:
                return btm8;
            case 9:
                return btm9;
            case 10:
            case 11:
            case 12:
                return btm10;
        }
        return null;
    }

    public void increaseCounter() {
        counter++;
    }
}
