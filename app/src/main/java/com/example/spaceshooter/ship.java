package com.example.spaceshooter;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ship {
    private int x, y, width, height;
    private int health;
    private int invincibleTime;
    boolean invincible;
    Bitmap ship;
    private int rectConstraint;

    public ship(int screenX, int screenY, Resources res){
        ship = BitmapFactory.decodeResource(res, R.drawable.ourship);
        rectConstraint = 50;
        this.y = screenY - 256;
        this.x = screenX/2;

        int i = 50;
        invincibleTime = 20;
        invincible = false;
        width = ship.getWidth() + i;
        height = ship.getHeight() + i;

        ship = Bitmap.createScaledBitmap(ship, width, height, false);
        this.health = 10;
    }

    public float getShipWidth() {
        return width;
    }

    public float getShipHeight() {
        return height;
    }
    Bitmap getShip() {
        return ship;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getRect() {
        return new Rect(x + rectConstraint, y + rectConstraint, x + width - rectConstraint, y + height - rectConstraint);
    }

    public void getHit() {
        if (!invincible) {
            health--;
            invincible = true;
        }
    }

    public void getDirectHit() {
        health--;
    }

    public void coldDown() {
        if (invincible) {
            invincibleTime--;
            if (invincibleTime == 0) {
                invincible = false;
                invincibleTime = 20;
            }
        }
    }

    public String getHealth() {
        return Integer.toString(health);
    }

    public int getHealthAsInt() {
        return health;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }
}
