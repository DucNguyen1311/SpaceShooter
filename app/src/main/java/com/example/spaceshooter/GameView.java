package com.example.spaceshooter;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private int playerScore, Highscore;
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
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    scoreSaverTable helper;
    String playerName;

    SQLiteDatabase database;
    private Context contextGame;
    public GameView (Context context, int screenX, int screenY, String name) {
        super(context);
        helper = new scoreSaverTable(context);
        contextGame = context;
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        Log.i("ihatemyself", name);
        playerName = name;
        Score score = helper.getHighestScore();

        if(score != null) {
            Highscore = score.getScore();
        }
        this.screenX = screenX;
        this.screenY = screenY;
        bulletSleep = 20;
        asteroidSleep = 17;
        playerScore = 0;
        backGround1 = new BackGround(screenX, screenY, getResources());
        backGround2 = new BackGround(screenX, screenY, getResources());
        Bitmap asteroidTemp = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
        asteroidSize = asteroidTemp.getWidth();
        ourShip = new Ship(screenX, screenY ,getResources());
        backGround2.y = screenY;

    }

    public void resetLife() {
        ourShip.forfeit();
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
    //this function is used to update the state of stuff
    private void update() throws InterruptedException {
        //this block is used to make the background move
        backGround1.y += 10 ;
        backGround2.y += 10 ;
        if (backGround1.y > screenY) {
            backGround1.y = -backGround1.background.getHeight();
        }
        if (backGround2.y > screenY) {
            backGround2.y = -backGround2.background.getHeight()  ;
        }
        // this block is used to count the spawn time of bullet
        if (bulletSleep > 0) {
            bulletSleep--;
        } else {
            bulletSleep = 20;
            Bullet bullet = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int)ourShip.getX(), (int)ourShip.getY(), contextGame);
            bullet.playShootSound();
            bullets.add(bullet);
        }
        //this if statement is used to count the spawn time of asteroids
        if (asteroidSleep > 0) {
            asteroidSleep--;
        } else {
            asteroidSleep = 20;
            Random generator = new Random();
            int spawnPoint = generator.nextInt((screenX - asteroidSize - 30) + 1 ) + 30;
            Asteroid asteroid = new Asteroid(spawnPoint, getResources(), contextGame);
            asteroids.add(asteroid);
        }
        //this for loop is used to update the coordinates of asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.getY() + asteroid.getHeight() > screenY) {
                asteroids.remove(asteroids.get(i));
                ourShip.getDirectHit();
                continue;
            }
            // this if statement is used to check if the asteroid is fully exploded;
            if (asteroid.isExploded()) {
                asteroids.remove(asteroids.get(i));
                continue;
            }
            // this if statement is used to check if the asteroid is still exploding;
            if (asteroid.isExplode()) {
                continue;
            }
            // this function is used to update the coords of asteroids
            asteroids.get(i).setY(asteroids.get(i).getY() + 20);
        }

        //this for loop is used to update the bullet state and checking intersection
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            // if the bullet go out of screen, remove it
            if (bullet.getY() < 0) {
                bullets.remove(bullet);
            }
            // this for loop is to check intersection of ship, asteroids and bullet
            for (Asteroid asteroid : asteroids) {
                Rect a = asteroid.getRect();
                Rect b = bullet.getRect();
                Rect c = ourShip.getRect();
                if (a.intersect(b)) {
                    bullets.remove(bullet);
                    asteroid.setExplode(true);
                    playerScore += 100;
                    asteroid.playExplodingSound();
                    if (playerScore>Highscore) Highscore = playerScore;
                }
                if (a.intersect(c)) {
                    asteroid.setExplode(true);
                    ourShip.getHit();
                }

            }
        }
        // whenever the ship is hit, it would be invincible in the next several seconds.
        // this function is to count that invincible frame
        ourShip.coldDown();
        // this function is used to update the bullet
        for (Bullet bullet : bullets) {
            bullet.setY(bullet.getY() - 20);
        }
        // gameover clause
        if (ourShip.getHealthAsInt() < 1) {
            helper.addNewScore(new Score(playerName, playerScore));
            Canvas canvas = getHolder().lockCanvas();
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(70);
            canvas.drawText("YOU LOSE!!!!!", screenX/2 - 200, screenY/2, paint1);
            getHolder().unlockCanvasAndPost(canvas);
            asteroids.clear();
            bullets.clear();
            Intent intent = new Intent(contextGame,LoseNotificationService.class);
            contextGame.startService(intent);
            Log.d("Player Score is: ", " " + playerScore);
            thread.sleep(2000);
            ((Activity)getContext()).finish();
        }
    }
    // this function is used to draw stuff into the canvas
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
            canvas.drawText("Score: " + playerScore, screenX - 550, 80, paint1);
            canvas.drawText("High Score: " + Highscore, screenX - 550, 160, paint1);
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
