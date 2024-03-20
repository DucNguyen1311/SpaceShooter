package com.example.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    boolean ended;
    int highScore;
    private int bulletSleep;
    private int asteroidSleep;
    private int onTouchInitialX;
    private int onTouchInitialY;
    private Ship ourShip;
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
        asteroidSleep = 17;
        highScore = 0;
        backGround1 = new BackGround(screenX, screenY, getResources());
        backGround2 = new BackGround(screenX, screenY, getResources());
        Bitmap asteroidTemp = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
        asteroidSize = asteroidTemp.getWidth();
        paint = new Paint();
        ourShip = new Ship(screenX, screenY ,getResources());
        //bullet = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight());
        backGround2.y = screenY;
    }

    @Override
    public void run() {

        while (isRunning) {

            try {
                update ();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            draw ();
            sleep ();

        }

    }

    private void update() throws InterruptedException {
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
            asteroidSleep = 17;
            Random generator = new Random();
            int spawnPoint = generator.nextInt((screenX - asteroidSize - 30) + 1 ) + 30;
            Asteroid asteroid = new Asteroid(spawnPoint, getResources());
            asteroids.add(asteroid);
        }
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.getY() + asteroid.getHeight() > screenY) {
                asteroids.remove(asteroids.get(i));
                ourShip.getDirectHit();
                continue;
            }
            if (asteroid.isExploded()) {
                asteroids.remove(asteroids.get(i));
                continue;
            }
            if (asteroid.isExplode()) {
                continue;
            }
            asteroids.get(i).setY(asteroids.get(i).getY() + 20);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.getY() < 0) {
                bullets.remove(bullet);
            }
            for (Asteroid asteroid : asteroids) {
                Rect a = asteroid.getRect();
                Rect b = bullet.getRect();
                Rect c = ourShip.getRect();
                if (a.intersect(b)) {
                    bullets.remove(bullet);
                    asteroid.setExplode(true);
                    highScore += 100;
                }
                if (a.intersect(c)) {
                    asteroid.setExplode(true);
                    ourShip.getHit();
                }

            }
        }
        ourShip.coldDown();
        for (Bullet bullet : bullets) {
            bullet.setY(bullet.getY() - 20);
        }
        if (ourShip.getHealthAsInt() < 1) {
            Canvas canvas = getHolder().lockCanvas();
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(70);
            canvas.drawText("YOU LOSE!!!!!", screenX/2 - 200, screenY/2, paint1);
            getHolder().unlockCanvasAndPost(canvas);
            thread.sleep(2000);
            ((Activity)getContext()).finish();
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
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(70);
            canvas.drawText("Health: " + ourShip.getHealth(), 30, 80, paint1);
            canvas.drawText("Score: " + highScore, screenX - 550, 80, paint1);
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
        ended = false;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() throws InterruptedException {
        isRunning = false;
        thread.join();
    }

    public boolean isEnded() {
        return ended;
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
