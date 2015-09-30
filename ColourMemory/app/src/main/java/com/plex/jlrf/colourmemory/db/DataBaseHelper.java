package com.plex.jlrf.colourmemory.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.plex.jlrf.colourmemory.model.Score;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "colours_memory.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Score.TABLE_NAME + " (" +
                Score._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Score.NICKNAME_KEY + "  TEXT NOT NULL, " +
                Score.SCORE_KEY + " INTEGER " +
                " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Score.TABLE_NAME);
        onCreate(db);
    }


}
