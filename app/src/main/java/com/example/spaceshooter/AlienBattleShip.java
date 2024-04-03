package com.example.spaceshooter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class AlienBattleShip extends Alien{
    private Bitmap btmp;
    private int liveTime;
    private int height;
    private int width;
    private boolean stillAlive;

    public AlienBattleShip(int x, Resources res, Context context) {
        super(x, 0);
        btmp = BitmapFactory.decodeResource(res, R.drawable.dreadnaught);
        liveTime = 150;
        stillAlive = true;
        super.explodingSfx = MediaPlayer.create(context, R.raw.exploding);
        width = btmp.getWidth() - 100;
        height = btmp.getHeight() - 100;
        btmp = Bitmap.createScaledBitmap(btmp, width, height, false);
    }

    public void liveCounter() {
        if (liveTime == 0) {
            stillAlive = false;
        } else {
            liveTime--;
        }
    }

    public Bitmap getBitmap() {
        return btmp;
    }

    public Rect getRect() {
        return new Rect(super.getX() + super.getRectConstraint(), super.getY() + super.getRectConstraint(), super.getX() + width - super.getRectConstraint(), super.getY() + height - super.getRectConstraint());
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isStillAlive() {
        return stillAlive;
    }

    public void playExplodingSound() {
        explodingSfx.start();
    }
}
