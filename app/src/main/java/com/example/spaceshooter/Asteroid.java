package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Asteroid {
    int explodeSequence = 13;
    int x,y;
    int width, height;
    boolean isExplode;
    boolean exploded;
    Bitmap asteroid;
    Bitmap explode1, explode2, explode3, explode4;

    public Asteroid(int x, Resources res) {
        this.x = x;
        this.y = 0;
        asteroid = BitmapFactory.decodeResource(res, R.drawable.asteroid);
        explode1 = BitmapFactory.decodeResource(res, R.drawable.explode1);
        explode2 = BitmapFactory.decodeResource(res, R.drawable.explode2);
        explode3 = BitmapFactory.decodeResource(res, R.drawable.explode3);
        explode4 = BitmapFactory.decodeResource(res, R.drawable.explode4);
        this.width = asteroid.getWidth();
        this.height = asteroid.getHeight();
        isExplode = false;
        exploded = false;
        asteroid = Bitmap.createScaledBitmap(asteroid, width, height, false);
        explode1 = Bitmap.createScaledBitmap(explode1, width, height, false);
        explode2 = Bitmap.createScaledBitmap(explode2, width, height, false);
        explode3 = Bitmap.createScaledBitmap(explode3, width, height, false);
        explode4 = Bitmap.createScaledBitmap(explode4, width, height, false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public Bitmap getBitmap() {
        if(isExplode) {
            if (explodeSequence == 2) {
                exploded = true;
            }
            explodeSequence--;
            if (explodeSequence >= 1 && explodeSequence < 4) {
                return explode4;
            }
            if (explodeSequence >= 4 && explodeSequence < 7) {
                return explode3;
            }
            if (explodeSequence >= 7 && explodeSequence < 10) {
                return explode2;
            }
            if (explodeSequence >= 10 && explodeSequence < 13) {
                return explode1;
            }
        }
        return asteroid;
    }

    public Rect getRect() {
        return new Rect(x + 10, y + 10, x + width - 10, y + height - 10);
    }

    public void setExplode(boolean explode) {
        this.isExplode = explode;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public boolean isExplode() {
        return isExplode;
    }

    public boolean isExploded() {
        return exploded;
    }
}
