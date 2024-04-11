package com.example.spaceshooter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.util.Random;

public class AlienNormalShip extends Alien{
    Bitmap btmp1,btmp2;
    private int height;
    private int width;
    private int counter;
    private int xVector;
    private int yVector;


    public AlienNormalShip(int x, Resources res, Context context) {
        super(x,0);
        btmp1 = BitmapFactory.decodeResource(res, R.drawable.enemyalien1);
        btmp2 = BitmapFactory.decodeResource(res, R.drawable.enemyalien2);
        this.height = btmp1.getHeight() + 50;
        this.width = btmp1.getWidth() + 50;
        counter = 0;
        xVector = 10;
        yVector = 5;
        btmp1 = Bitmap.createScaledBitmap(btmp1, width, height, false);
        btmp2 = Bitmap.createScaledBitmap(btmp2, width, height, false);
    }

    public Bitmap getBitmap() {
        if (counter > 3) {
            counter = 0;
        }
        if (counter >= 0 && counter < 2) {
            counter++;
            return btmp1;
        }
        if (counter >= 2) {
            counter++;
            return btmp2;
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setxVector(int xVector) {
        this.xVector = xVector;
    }

    public void setyVector(int yVector) {
        this.yVector = yVector;
    }

    public int getxVector() {
        return xVector;
    }

    public int getyVector() {
        return yVector;
    }

    public Rect getRect() {
        return new Rect(super.getX() + super.getRectConstraint(), super.getY() + super.getRectConstraint(), super.getX() + width - super.getRectConstraint(), super.getY() + height - super.getRectConstraint());
    }
}
