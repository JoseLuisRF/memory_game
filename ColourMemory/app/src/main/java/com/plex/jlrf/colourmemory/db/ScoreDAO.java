package com.plex.jlrf.colourmemory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.plex.jlrf.colourmemory.model.Score;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class ScoreDAO {

    private static ScoreDAO mInstance;
    private DataBaseHelper mDataBaseHelper;

    private ScoreDAO(Context context){
        mDataBaseHelper = new DataBaseHelper(context);

    }

    public static ScoreDAO newInstance(Context context){
        if(mInstance == null){
            mInstance = new ScoreDAO(context);
        }
        return mInstance;
    }

    public long inertScore(Score score) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Score.NICKNAME_KEY, score.getNickname());
        contentValues.put(Score.SCORE_KEY, score.getScore());

        return db.insert(Score.TABLE_NAME, null, contentValues);
    }

    public Cursor getScores() {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        return db.query(Score.TABLE_NAME, null, null, null, null, null, Score.SCORE_KEY + " DESC");
    }

    public Score getHighScore(){
        Cursor cursor = getScores();
        Score score = null;
        if(cursor.moveToFirst()){
            String nickName = cursor.getString(cursor.getColumnIndex(Score.NICKNAME_KEY));
            int points = cursor.getInt(cursor.getColumnIndex(Score.SCORE_KEY));
            score = new Score(nickName, points);
        }
        return score;
    }




}
