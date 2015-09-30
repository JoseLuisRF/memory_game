package com.plex.jlrf.colourmemory.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.plex.jlrf.colourmemory.MainActivity;
import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.db.ScoreDAO;
import com.plex.jlrf.colourmemory.model.Score;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class HighScoreActivity extends AppCompatActivity {

    private ListView mHighScoreListView;
    private ScoreDAO mScoreDAO;
    private SimpleCursorAdapter mAdapter;

    public final static String[] FROM_COLUMNS = {Score.NICKNAME_KEY, Score.SCORE_KEY};
    private final static int[] TO_IDS = {
            R.id.tv_nickname,
            R.id.tv_score_value
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        mScoreDAO = ScoreDAO.newInstance(this);
        setupUI();
    }



    private void setupUI(){
        mHighScoreListView = (ListView)this.findViewById(R.id.lv_scores);
        mAdapter = new SimpleCursorAdapter(this, R.layout.item_score, null,FROM_COLUMNS, TO_IDS, 0);
        mHighScoreListView.setAdapter(mAdapter);
        mAdapter.swapCursor(mScoreDAO.getScores());

    }

}
