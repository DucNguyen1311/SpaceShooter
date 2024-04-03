package com.example.spaceshooter;

import android.graphics.Rect;
import android.media.MediaPlayer;

public class Alien {
    private int x;
    private int y;

    MediaPlayer explodingSfx;
    private int rectConstraint;
    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        this.rectConstraint = 50;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getRectConstraint() {
        return rectConstraint;
    }

}
