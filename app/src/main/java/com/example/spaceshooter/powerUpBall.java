package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class powerUpBall {
    private int x,y;
    private Bitmap btm1,btm2,btm3,btm4,btm5,btm6;

    private int height;
    private int width;

    private int counter;
    private int rectConstraint;
    private int constraint;

    public powerUpBall(Resources res, int x, int y) {
        this.x = x;
        this.y = y;
        counter = -1;
        rectConstraint = 50;
        constraint = 950;
        btm1 = BitmapFactory.decodeResource(res, R.drawable.ball1);
        btm2 = BitmapFactory.decodeResource(res, R.drawable.ball2);
        btm3 = BitmapFactory.decodeResource(res, R.drawable.ball3);
        btm4 = BitmapFactory.decodeResource(res, R.drawable.ball4);
        btm5 = BitmapFactory.decodeResource(res, R.drawable.ball5);
        btm6 = BitmapFactory.decodeResource(res, R.drawable.ball6);
        width = btm1.getWidth();
        height = btm1.getHeight();
        btm1 = Bitmap.createScaledBitmap(btm1, width - constraint, height - constraint, false);
        btm2 = Bitmap.createScaledBitmap(btm2, width - constraint, height - constraint, false);
        btm3 = Bitmap.createScaledBitmap(btm3, width - constraint, height - constraint, false);
        btm4 = Bitmap.createScaledBitmap(btm4, width - constraint, height - constraint, false);
        btm5 = Bitmap.createScaledBitmap(btm5, width - constraint, height - constraint, false);
        btm6 = Bitmap.createScaledBitmap(btm6, width - constraint, height - constraint, false);
        width-=constraint - 250;
        height-=constraint - 250;
    }

    public Bitmap getBitmap() {
        counter++;
        if (counter >= 24) {
            counter = 0;
        } else if (counter < 4) {
            return btm1;
        } else if (counter < 8) {
            return btm2;
        } else if (counter < 12) {
            return btm3;
        } else if (counter < 16) {
            return btm4;
        } else if (counter < 20){
            return btm5;
        } else {
            return btm6;
        }
        return btm6;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getRect() {
        return new Rect(x + rectConstraint, y + rectConstraint, x + width - rectConstraint, y + height - rectConstraint);
    }
}
