package com.example.spaceshooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ship {
    private int x, y, width, height;
    private int health;

    Bitmap ship;

    public ship(int screenX, int screenY, Resources res){
        ship = BitmapFactory.decodeResource(res, R.drawable.ourship);
        this.y = screenY - 256;
        this.x = screenX/2;

        int i = 50;

        width = ship.getWidth() + i;
        height = ship.getHeight() + i;

        ship = Bitmap.createScaledBitmap(ship, width, height, false);
        this.health = 3;
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

}
