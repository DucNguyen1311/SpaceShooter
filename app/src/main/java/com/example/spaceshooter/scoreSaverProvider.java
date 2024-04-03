package com.example.spaceshooter;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class scoreSaverProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.spaceshooter.scoreSaverProvider";
    static final String CONTENT_PATH = "backupdata";
    static final String URL = "content://" + AUTHORITY + "/" + CONTENT_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int URI_ALL_ITEMS_CODE = 1;
    static final int URI_ONE_ITEM_CODE = 2;
    static final UriMatcher uriMatcher;
    private static final String TABLE_NAME = "scores";
    private static HashMap<String, String> SCORE_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_ALL_ITEMS_CODE);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);
    }


    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        scoreSaverTable database = new scoreSaverTable(getContext());
        db = database.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                qb.setProjectionMap(SCORE_PROJECTION_MAP);
                break;

            case URI_ONE_ITEM_CODE:
                qb.appendWhere("name" + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = "name";
        }

        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case URI_ONE_ITEM_CODE:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME, "name" + " = " + id +
                        (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            case URI_ONE_ITEM_CODE:
                count = db.update(TABLE_NAME, values,
                        "name" + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
