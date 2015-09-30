package com.plex.jlrf.colourmemory.model;

import android.provider.BaseColumns;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class Score implements BaseColumns{
    public static final String TABLE_NAME = "highscore";

    public static final String SCORE_KEY = "score";
    public static final String NICKNAME_KEY = "nickname";

    private int score;
    private String nickname;

    public Score(String nickname, int score){
        this.nickname = nickname;
        this.score = score;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
