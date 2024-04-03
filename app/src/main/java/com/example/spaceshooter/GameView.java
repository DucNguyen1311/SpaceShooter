package com.example.spaceshooter;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

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
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends SurfaceView implements Runnable {
    private boolean isShielded;
    private boolean isDoubleShooted;
    private boolean isTripleShooted;
    private boolean isEnded;
    private final int BULLET_STRAIGHT = 1;
    private final int BULLET_DOWN = 2;
    private final int BULLET_LEFT_30_DEGREE = 3;
    private final int BULLET_RIGHT_30_DEGREE = 4;

    private int tripleShootCounter;
    private int doubleShootCounter;

    private int playerScore, Highscore;
    private int bulletSleep;
    private int enemySleep;
    private int onTouchInitialX;
    private int onTouchInitialY;
    private Ship ourShip;
    private BackGround backGround1, backGround2;
    private boolean isRunning ;
    private Thread thread;
    private int screenX, screenY;
    private int asteroidSize;
    private shield shield;
    private Map<Bullet, Integer> bullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Explosion> explosions;
    private ArrayList<Alien> aliens;
    private ArrayList<powerUpBall> powerUpBalls;
    scoreSaverTable helper;
    String playerName;
    String HighscoreHolder;

    healthBar healthBar;

    SQLiteDatabase database;
    private Context contextGame;
    public GameView (Context context, int screenX, int screenY, String name) {
        super(context);
        isShielded = false;
        shield = new shield(getResources());
        healthBar = new healthBar(getResources());
        helper = new scoreSaverTable(context);
        contextGame = context;
        bullets = new ConcurrentHashMap<>();
        asteroids = new ArrayList<Asteroid>();
        explosions = new ArrayList<>();
        powerUpBalls = new ArrayList<>();
        aliens = new ArrayList<>();
        Log.i("ihatemyself", name);
        playerName = name;
        Score score = helper.getHighestScore();

        if(score != null) {
            Highscore = score.getScore();
            HighscoreHolder = score.getName();
        }
        this.screenX = screenX;
        this.screenY = screenY;
        bulletSleep = 20;
        enemySleep = 17;
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
        movingBackGround();
        enemySpawnCalculator();
        updateEnemyAndCheckingIntersection();
        coldown();
        updateState();
        bulletSpawnCalculator();
        gameOverClause();
    }
    // this function is used to draw stuff into the canvas
    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGround1.background, backGround1.x, backGround1.y, null);
            canvas.drawBitmap(backGround2.background, backGround2.x, backGround2.y, null);
            canvas.drawBitmap(ourShip.getShip(), ourShip.getX(), ourShip.getY(), null);
            for(Explosion explosion : explosions) {
                canvas.drawBitmap(explosion.getExplosion(), explosion.getX(), explosion.getY(), null);
            }
            for (Map.Entry<Bullet, Integer> entry : bullets.entrySet()) {
                canvas.drawBitmap(entry.getKey().getBullet(), entry.getKey().getX(), entry.getKey().getY(), null);
            }
            for (Asteroid asteroid : asteroids) {
                canvas.drawBitmap(asteroid.getBitmap(), asteroid.getX(), asteroid.getY(), null);
            }
            for (Alien alien : aliens) {
                if (alien.getClass() == AlienNormalShip.class) {
                    AlienNormalShip drawAlien = (AlienNormalShip) alien;
                    canvas.drawBitmap(drawAlien.getBitmap(), drawAlien.getX(), drawAlien.getY(), null);
                }
                if (alien.getClass() == AlienBattleShip.class) {
                    AlienBattleShip drawAlien = (AlienBattleShip) alien;
                    canvas.drawBitmap(drawAlien.getBitmap(), drawAlien.getX(), drawAlien.getY(), null);

                }
            }
            for (powerUpBall powerUpBall : powerUpBalls) {
                canvas.drawBitmap(powerUpBall.getBitmap(), powerUpBall.getX(), powerUpBall.getY(), null);
            }
            if (isShielded) {
                canvas.drawBitmap(shield.getBitmap(), ourShip.getX() - 10, ourShip.getY(), null);
            }
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(70);
            canvas.drawBitmap(healthBar.getBitmap(), 50, 50, null);
            canvas.drawText("Score: " + playerScore, screenX - 550, 80, paint1);
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
    private void gameOverClause() throws InterruptedException {
        if (ourShip.getHealthAsInt() < 1) {
            helper.addNewScore(new Score(playerName, playerScore));
            Canvas canvas = getHolder().lockCanvas();
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(70);
            canvas.drawText("YOU LOSE!!!!!", screenX / 2 - 200, screenY / 2, paint1);
            getHolder().unlockCanvasAndPost(canvas);
            asteroids.clear();
            bullets.clear();
            Intent intent = new Intent(contextGame, LoseNotificationService.class);
            contextGame.startService(intent);
            isEnded = true;
            Intent i = new Intent(contextGame, Game_Over.class);
            i.putExtra("playerScore", playerScore);
            i.putExtra("highestScore", Highscore);
            i.putExtra("getholder", HighscoreHolder);
            Log.d("bundle: " , playerScore + " " + Highscore + " " + HighscoreHolder);
            contextGame.startActivity(i);
            ((Activity)getContext()).finish();
            Log.d("Player Score is: ", " " + playerScore);
        }
    }

    public boolean isEnded() {
        return isEnded;
    }

    public int getHighscore() {
        return Highscore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String getHighscoreHolder() {
        return HighscoreHolder;
    }

    private void updateState() {
        explosions.removeIf(Explosion::isFinish);

        for (Map.Entry<Bullet, Integer> entry : bullets.entrySet()) {
            if (entry.getValue() == BULLET_STRAIGHT) {
                entry.getKey().setY(entry.getKey().getY() - 20);
            }
            if (entry.getValue() == BULLET_DOWN) {
                entry.getKey().setY(entry.getKey().getY() + 20);
            }
            if (entry.getValue() == BULLET_LEFT_30_DEGREE) {
                entry.getKey().setY(entry.getKey().getY() - 20);
                entry.getKey().setX(entry.getKey().getX() - 13);
            }
            if (entry.getValue() == BULLET_RIGHT_30_DEGREE) {
                entry.getKey().setY(entry.getKey().getY() - 20);
                entry.getKey().setX(entry.getKey().getX() + 13);
            }

        }

        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getClass() == AlienNormalShip.class) {
                AlienNormalShip enemy = (AlienNormalShip) aliens.get(i);
                if (enemy.getY() + enemy.getHeight() + 20 > screenY/4 || (enemy.getY() + 20 < 0)) {
                    enemy.setyVector(-enemy.getyVector());
                }
                if (enemy.getX() + enemy.getWidth() + 20 > screenX || enemy.getX() + 10 < 0) {
                    enemy.setxVector(-enemy.getxVector());
                }
                enemy.setX(enemy.getX() + enemy.getxVector());
                enemy.setY(enemy.getY() + enemy.getyVector());
            }
            if (aliens.get(i).getClass() == AlienBattleShip.class) {
                AlienBattleShip alienBattleShip = (AlienBattleShip) aliens.get(i);
                if (alienBattleShip.isStillAlive()) {
                    alienBattleShip.liveCounter();
                } else {
                    aliens.remove(alienBattleShip);
                }
            }
        }

        for (int i = 0 ; i < powerUpBalls.size(); i++) {
            powerUpBall powerUpBall = powerUpBalls.get(i);
            if (powerUpBall.getY() + powerUpBall.getHeight() > screenY) {
                powerUpBalls.remove(powerUpBall);
                continue;
            }
            powerUpBall.setY(powerUpBall.getY() + 10);
        }

        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.getY() + asteroid.getHeight() > screenY) {
                asteroids.remove(asteroids.get(i));
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
    }

    private void updateEnemyAndCheckingIntersection() {
        for (Map.Entry<Bullet, Integer> entry : bullets.entrySet()) {
            Bullet bullet = entry.getKey();
            // if the bullet go out of screen, remove it
            if (bullet.getY() < 0 || bullet.getY() + bullet.getHeight() > screenY) {
                bullets.remove(bullet);
            }
            // this for loop is to check intersection of ship, asteroids and bullet
            for (Asteroid asteroid : asteroids) {
                Rect a = asteroid.getRect();
                Rect b = bullet.getRect();
                Rect c = ourShip.getRect();
                if (a.intersect(b) && (entry.getValue() == BULLET_STRAIGHT || entry.getValue() == BULLET_LEFT_30_DEGREE || entry.getValue() == BULLET_RIGHT_30_DEGREE)) {
                    bullets.remove(bullet);
                    explosions.add(new Explosion(getResources(), bullet.getX(), bullet.getY() - 50) );
                    asteroid.setExplode(true);
                    playerScore += 100;
                    asteroid.playExplodingSound();
                    if (playerScore>Highscore) {
                        Highscore = playerScore;
                        HighscoreHolder = playerName;
                    }
                }
                if (b.intersect(c) && entry.getValue() == BULLET_DOWN) {
                    bullets.remove(bullet);
                    explosions.add(new Explosion(getResources(), (int) ourShip.getX(), (int) (ourShip.getY()-50)));
                    if (!isShielded) {
                        if (!ourShip.isInvincible()) {
                            healthBar.increaseCounter();
                        }
                        ourShip.getHit();
                    }
                    Log.d("Get hit", "intersect with enemy bullet");
                }
                if (a.intersect(c)) {
                    asteroid.setExplode(true);
                    explosions.add(new Explosion(getResources(), (int) ourShip.getX(), (int) (ourShip.getY()-50)));
                    asteroid.playExplodingSound();
                    if (!isShielded) {
                        if (!ourShip.isInvincible()) {
                            healthBar.increaseCounter();
                        }
                        ourShip.getHit();
                    }
                    Log.d("Get hit", "intersect with asteroid");
                }
            }

            for (int i = 0; i < aliens.size(); i++) {
                Alien alien = aliens.get(i);
                if (alien.getClass() == AlienNormalShip.class) {
                   AlienNormalShip enemy = (AlienNormalShip) alien;
                    Rect a = enemy.getRect();
                    Rect b = bullet.getRect();
                    Rect c = ourShip.getRect();
                    if (a.intersect(b) && entry.getValue() == BULLET_STRAIGHT) {
                        bullets.remove(bullet);
                        Random rand = new Random();
                        int n = rand.nextInt(2);
                        if (n == 1) {
                            powerUpBalls.add(new powerUpBall(getResources(), alien.getX(), alien.getY()));
                        }
                        explosions.add(new Explosion(getResources(), bullet.getX(), bullet.getY() - 50) );
                        aliens.remove(alien);
                        enemy.playExplodingSound();
                        playerScore += 200;
                        if (playerScore>Highscore) {
                            Highscore = playerScore;
                            HighscoreHolder = playerName;
                        }
                    }
                    if (a.intersect(c)) {
                        aliens.remove(alien);
                        explosions.add(new Explosion(getResources(), (int) ourShip.getX(), (int) (ourShip.getY()-50)));
                        if (!isShielded) {
                            if (!ourShip.isInvincible()) {
                                healthBar.increaseCounter();
                            }
                            ourShip.getHit();
                        }
                        enemy.playExplodingSound();
                        Log.d("Get hit", "Intersect with enemy");
                    }
                }
            }
        }
        for (int i = 0; i < powerUpBalls.size(); i++) {
            powerUpBall powerUpBall = powerUpBalls.get(i);
            Rect a = powerUpBall.getRect();
            Rect b = ourShip.getRect();
            if (a.intersect(b)) {
                Log.d("intersect", "powerBall collected");
                powerUpBalls.remove(powerUpBall);
                Random rand = new Random();
                int n = rand.nextInt(3);
                if (n == 0) {
                    if (isShielded) {
                        shield.resetTimer();
                    } else {
                        isShielded = true;
                    }
                }
                if (n == 1) {
                    if (isDoubleShooted) {
                        doubleShootCounter = 200;
                    } else {
                        isDoubleShooted = true;
                    }
                }
                else {
                    if (isTripleShooted) {
                        tripleShootCounter = 200;
                    } else {
                            isTripleShooted = true;
                    }
                }
            }
        }
    }


    private void enemySpawnCalculator() {
        if (enemySleep > 0) {
            enemySleep--;
        } else {
            enemySleep = 20;
            Random generator = new Random();
            int rng = generator.nextInt(100);
            if (rng < 75) {
                spawnAsteroid();
            } else if (rng < 95){
                spawnAlienNormalShip();
            } else {
                spawnBattleShip();
            }
        }
    }

    private void spawnAsteroid() {
        Random generator = new Random();
        int spawnPoint = generator.nextInt((screenX - asteroidSize - 30) + 1 ) + 30;
        Asteroid asteroid = new Asteroid(spawnPoint, getResources(), contextGame);
        asteroids.add(asteroid);
    }

    private void spawnAlienNormalShip() {
        Random generator = new Random();
        int spawnPoint = generator.nextInt((screenX - asteroidSize - 30) + 1 ) + 30;
        AlienNormalShip alien = new AlienNormalShip(spawnPoint, getResources(), contextGame);
        aliens.add(alien);
    }

    private void spawnBattleShip() {
        Random generator = new Random();
        int spawnPoint = generator.nextInt((screenX - asteroidSize - 30) + 1 ) + 30;
        AlienBattleShip alien = new AlienBattleShip(spawnPoint, getResources(), contextGame);
        aliens.add(alien);
    }

    private void coldown() {
        ourShip.coldDown();
        if (isShielded) {
            shield.countingShield();
            if (shield.isEnded()) {
                isShielded = false;
                shield.setEnded(false);
            }
        }
        if (isTripleShooted) {
            tripleShootCounter--;
            if (tripleShootCounter == 0) {
                tripleShootCounter = 200;
                isTripleShooted = false;
                Log.d("triple shoot", "Ended");
            }
        }
        if (isDoubleShooted) {
            doubleShootCounter--;
            if (doubleShootCounter == 0) {
                doubleShootCounter = 200;
                isDoubleShooted = false;
            }
        }
    }

    private void movingBackGround() {
        backGround1.y += 10 ;
        backGround2.y += 10 ;
        if (backGround1.y > screenY) {
            backGround1.y = -backGround1.background.getHeight();
        }
        if (backGround2.y > screenY) {
            backGround2.y = -backGround2.background.getHeight()  ;
        }
    }

    private void bulletSpawnCalculator() {
        if (bulletSleep > 0) {
            bulletSleep--;
        } else if(isDoubleShooted && isTripleShooted) {
            bulletSleep = 20;
            Bullet bulletMid1 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX(), (int) ourShip.getY(), contextGame, 1);
            Bullet bulletMid2 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() + 30, (int) ourShip.getY(), contextGame, 1);
            Bullet bulletMid3 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() - 30, (int) ourShip.getY(), contextGame, 1);
            Bullet bulletLeft = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() - 60, (int) ourShip.getY(), contextGame, 3);
            Bullet bulletRight = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() + 60, (int) ourShip.getY(), contextGame, 4);
            bulletMid1.playShootSound();
            bullets.put(bulletMid1, BULLET_STRAIGHT);
            bullets.put(bulletMid2, BULLET_STRAIGHT);
            bullets.put(bulletMid3, BULLET_STRAIGHT);
            bullets.put(bulletLeft, BULLET_LEFT_30_DEGREE);
            bullets.put(bulletRight, BULLET_RIGHT_30_DEGREE);
        } else if (isTripleShooted) {
            bulletSleep = 20;
            Bullet bulletMid1 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX(), (int) ourShip.getY(), contextGame, 1);
            Bullet bulletLeft = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() - 30, (int) ourShip.getY(), contextGame, 3);
            Bullet bulletRight = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() + 30, (int) ourShip.getY(), contextGame, 4);
            bulletMid1.playShootSound();
            bullets.put(bulletMid1, BULLET_STRAIGHT);
            bullets.put(bulletLeft, BULLET_LEFT_30_DEGREE);
            bullets.put(bulletRight, BULLET_RIGHT_30_DEGREE);
        } else if (isDoubleShooted) {
            bulletSleep = 20;
            Bullet bulletMid2 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() + 30, (int) ourShip.getY(), contextGame, 1);
            Bullet bulletMid3 = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX() - 30, (int) ourShip.getY(), contextGame, 1);
            bulletMid2.playShootSound();
            bullets.put(bulletMid2, BULLET_STRAIGHT);
            bullets.put(bulletMid3, BULLET_STRAIGHT);
        } else {
            bulletSleep = 20;
            Bullet bullet = new Bullet(getResources(), (int) ourShip.getShipWidth(), (int) ourShip.getShipHeight(), (int) ourShip.getX(), (int) ourShip.getY(), contextGame, 1);
            bullet.playShootSound();
            bullets.put(bullet, BULLET_STRAIGHT);
        }
        if (bulletSleep == 20) {
            for (int i = 0; i < aliens.size(); i++) {
                if (aliens.get(i).getClass() == AlienNormalShip.class) {
                    AlienNormalShip enemy = (AlienNormalShip) aliens.get(i);
                    Bullet bulletEnemy = new Bullet(getResources(), (int) enemy.getWidth(), (int) enemy.getHeight(), (int) enemy.getX(), (int) enemy.getY(), contextGame, 2);
                    bulletEnemy.playShootSound();
                    bullets.put(bulletEnemy, BULLET_DOWN);
                }
                if (aliens.get(i).getClass() == AlienBattleShip.class) {
                    AlienBattleShip enemy = (AlienBattleShip) aliens.get(i);
                    Bullet bulletEnemyLeft = new Bullet(getResources(), (int) enemy.getWidth() - 100, (int) enemy.getHeight() - 50, (int) enemy.getX() - 50, (int) enemy.getY(), contextGame, 2);
                    Bullet bulletEnemyRight = new Bullet(getResources(), (int) enemy.getWidth() - 100, (int) enemy.getHeight() - 50, (int) enemy.getX() + 30, (int) enemy.getY(), contextGame, 2);
                    bulletEnemyRight.playShootSound();
                    bullets.put(bulletEnemyLeft, BULLET_DOWN);
                    bullets.put(bulletEnemyRight, BULLET_DOWN);
                }
            }
        }
    }

}
