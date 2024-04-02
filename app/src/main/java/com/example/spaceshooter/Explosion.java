package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    private Bitmap explosion1, explosion2, explosion3, explosion4, explosion5, explosion6;
    private int explosionTimer ;
    private boolean finish;
    private int x,y;
    public Explosion (Resources res, int x, int y) {
        this.x = x;
        this.y = y;
        explosion1 = BitmapFactory.decodeResource(res, R.drawable.explosionf1);
        explosion2 = BitmapFactory.decodeResource(res, R.drawable.explosionf2);
        explosion3 = BitmapFactory.decodeResource(res, R.drawable.explosionf3);
        explosion4 = BitmapFactory.decodeResource(res, R.drawable.explosionf4);
        explosion5 = BitmapFactory.decodeResource(res, R.drawable.explosionf5);
        explosion6 = BitmapFactory.decodeResource(res, R.drawable.explosionf6);
        explosionTimer = 0;
        finish = false;
    }

    public Bitmap getExplosion() {
        explosionTimer++;
        if (explosionTimer >= 18) {
            finish = true;
        }
        if (explosionTimer > 0 && explosionTimer < 4) {
            return explosion1;
        }
        if (explosionTimer >= 4 && explosionTimer < 7) {
            return explosion2;
        }
        if (explosionTimer >= 7 && explosionTimer < 10) {
            return explosion3;
        }
        if (explosionTimer >= 10 && explosionTimer < 13) {
            return explosion4;
        }
        if (explosionTimer >=13 && explosionTimer < 16) {
            return explosion5;
        }
        if (explosionTimer >= 16 && explosionTimer < 19) {
            return explosion6;
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFinish() {
        return finish;
    }
}
