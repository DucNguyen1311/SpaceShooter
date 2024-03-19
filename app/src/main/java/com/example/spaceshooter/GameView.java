package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.constraintlayout.solver.widgets.Rectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private int bulletSleep;
    private int asteroidSleep;
    private int onTouchInitialX;
    private int onTouchInitialY;
    private ship ourShip;
    private BackGround backGround1, backGround2;
    private boolean isRunning ;
    private Thread thread;
    private int screenX, screenY;
    private int asteroidSize;
    private Paint paint;
    private float screenRatioX, screenRatioY;
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    public GameView (Context context, int screenX, int screenY) {
        super(context);
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        Log.i("ihatemyself","Gameview initialize");
        this.screenX = screenX;
        this.screenY = screenY;
        bulletSleep = 20;
        asteroidSleep = 10;
        backGround1 = new BackGround(screenX, screenY, getResources());
        backGround2 = new BackGround(screenX, screenY, getResources());
        Bitmap asteroidTemp = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
        asteroidSize = asteroidTemp.getWidth();
        paint = new Paint();
        ourShip = new ship(screenX, screenY ,getResources());
        //bullet = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight());
        backGround2.y = screenY;
    }

    @Override
    public void run() {

        while (isRunning) {

            update ();
            draw ();
            sleep ();

        }

    }

    private void update() {
        backGround1.y += 10 ;
        backGround2.y += 10 ;
        if (backGround1.y > screenY) {
            backGround1.y = -backGround1.background.getHeight();
        }
        if (backGround2.y > screenY) {
            backGround2.y = -backGround2.background.getHeight()  ;
        }
        if (bulletSleep > 0) {
            bulletSleep--;
        } else {
            bulletSleep = 20;
            Bullet bullet = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int)ourShip.getX(), (int)ourShip.getY());
            bullets.add(bullet);
        }
        if (asteroidSleep > 0) {
            asteroidSleep--;
        } else {
            asteroidSleep = 10;
            Random generator = new Random();
            int spawnPoint = generator.nextInt() % (screenX - asteroidSize);
            Asteroid asteroid = new Asteroid(spawnPoint, getResources());
            asteroids.add(asteroid);
        }
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.getY() + asteroid.getHeight() > screenY) {
                asteroids.remove(asteroids.get(i));
            }
            if (asteroid.isExploded()) {
                asteroids.remove(asteroids.get(i));
                continue;
            }
            if (asteroid.isExplode()) {
                continue;
            }
            asteroids.get(i).setY(asteroids.get(i).getY() + 30);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.getY() < 0) {
                bullets.remove(bullet);
            }
            for (Asteroid asteroid : asteroids) {
                Rect a = asteroid.getRect();
                Rect b = bullet.getRect();
                if (a.intersect(b)) {
                    bullets.remove(bullet);
                    asteroid.setExplode(true);
                }
            }
        }
        for (Bullet bullet : bullets) {
            bullet.setY(bullet.getY() - 20);
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGround1.background, backGround1.x, backGround1.y, null);
            canvas.drawBitmap(backGround2.background, backGround2.x, backGround2.y, null);
            canvas.drawBitmap(ourShip.getShip(), ourShip.getX(), ourShip.getY(), null);
            for (Bullet bullet : bullets) {
                canvas.drawBitmap(bullet.getBullet(), bullet.getX(), bullet.getY(), null);
            }
            for (Asteroid asteroid : asteroids) {
                canvas.drawBitmap(asteroid.getBitmap(), asteroid.getX(), asteroid.getY(), null);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }

    }
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void resume() {
        Log.i("thread", "thread started");
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() throws InterruptedException {
        isRunning = false;
        thread.join();
    }

    @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchInitialX = (int) event.getX();
                    onTouchInitialY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int diffX = (int) event.getX() - onTouchInitialX;
                    int diffY = (int) event.getY() - onTouchInitialY;
                    onTouchInitialX = (int) event.getX();
                    onTouchInitialY = (int) event.getY();
                    if ((int)ourShip.getY() + (int)ourShip.getShipHeight() + diffY < screenY && (int)ourShip.getY() + diffY > 0) {
                        ourShip.setY((int)ourShip.getY() + diffY);
                    }
                    if ((int)ourShip.getX() + (int)ourShip.getShipWidth() + diffX < screenX && (int)ourShip.getX() + diffX> 0) {
                        ourShip.setX((int)ourShip.getX() + diffX);
                    }
                    break;
            }
            return true;
    }

}
