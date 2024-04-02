package com.example.spaceshooter;

import android.graphics.Rect;

public class Alien {
    private int health;
    private int x;
    private int y;

    private int rectConstraint;
    public Alien(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
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

    public int getHealth() {
        return health;
    }
}
