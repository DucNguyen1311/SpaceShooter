package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class shield {
    private int width, height;

    private Bitmap btm1,btm2,btm3,btm4,btm5,btm6,btm7,btm8,btm9,btm10,btm11;
    private int counter;
    private int timer;
    private int rectConstraint;
    private boolean isEnded;

    public shield(Resources res) {
        timer = 200;
        counter = 0;
        isEnded = false;
        rectConstraint = 50;
        btm1 = BitmapFactory.decodeResource(res, R.drawable.shield1);
        btm2 = BitmapFactory.decodeResource(res, R.drawable.shield2);
        btm3 = BitmapFactory.decodeResource(res, R.drawable.shield3);
        btm4 = BitmapFactory.decodeResource(res, R.drawable.shield4);
        btm5 = BitmapFactory.decodeResource(res, R.drawable.shield5);
        btm6 = BitmapFactory.decodeResource(res, R.drawable.shield6);
        btm7 = BitmapFactory.decodeResource(res, R.drawable.shield7);
        btm8 = BitmapFactory.decodeResource(res, R.drawable.shield8);
        btm9 = BitmapFactory.decodeResource(res, R.drawable.shield9);
        btm10 = BitmapFactory.decodeResource(res, R.drawable.shield10);
        btm11 = BitmapFactory.decodeResource(res, R.drawable.shield11);
        width += 200;
        height+= 200;
        btm1 = Bitmap.createScaledBitmap(btm1, width, height, false);
        btm2 = Bitmap.createScaledBitmap(btm2, width, height, false);
        btm3 = Bitmap.createScaledBitmap(btm3, width, height, false);
        btm4 = Bitmap.createScaledBitmap(btm4, width, height, false);
        btm5 = Bitmap.createScaledBitmap(btm5, width, height, false);
        btm6 = Bitmap.createScaledBitmap(btm6, width, height, false);
        btm7 = Bitmap.createScaledBitmap(btm7, width, height, false);
        btm8 = Bitmap.createScaledBitmap(btm8, width, height, false);
        btm9 = Bitmap.createScaledBitmap(btm9, width, height, false);
        btm10 = Bitmap.createScaledBitmap(btm10, width, height, false);
        btm11 = Bitmap.createScaledBitmap(btm11, width, height, false);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void countingShield() {
        if (timer == 0) {
            isEnded = true;
            timer = 200;
        } else {
            timer--;
        }
    }
    public Bitmap getBitmap() {
        counter++;
        if (counter >= 55) {
            counter = 0;
        } else if (counter < 5) {
            return btm1;
        } else if (counter < 10) {
            return btm2;
        } else if (counter < 15) {
            return btm3;
        } else if (counter < 20) {
            return btm4;
        } else if (counter < 25) {
            return btm5;
        } else if (counter < 30) {
            return btm6;
        } else if (counter < 35) {
            return btm7;
        } else if (counter < 40) {
            return btm8;
        } else if (counter < 45) {
            return btm9;
        } else if (counter < 50) {
            return btm10;
        } else {
            return btm11;
        }
        return btm11;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }
    public void resetTimer() {
        timer = 200;
    }

    public Rect getShieldRect(int x, int y) {
        return new Rect(x + rectConstraint, y + rectConstraint, x + width - rectConstraint, y + height - rectConstraint);
    }

}
