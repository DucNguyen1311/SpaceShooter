package com.example.spaceshooter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

public final class scoreSaverTable extends SQLiteOpenHelper{

    private static final String DATABASE_NAME ="scoreSaver";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "scores";

    private static final String KEY_NAME = "name";
    private static final String KEY_score = "score";

    public scoreSaverTable(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + KEY_NAME + " TEXT ," + KEY_score + " INTEGER);";
        Log.d(query, "created table");
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXIST %s;", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public void addNewScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName());
        values.put(KEY_score, score.getScore());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public Score getHighestScore() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT *, MAX(score) FROM scores", null);

        if (cursor != null) {
            cursor.moveToFirst();
            db.close();
            return new Score(cursor.getString(0), cursor.getInt(1));
        }
        Log.d("Highscore Error", "no Highscore Registered");
        db.close();
        return null;
    }

}
