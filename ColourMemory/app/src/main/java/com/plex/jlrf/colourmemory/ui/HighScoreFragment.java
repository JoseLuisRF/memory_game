package com.plex.jlrf.colourmemory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.plex.jlrf.colourmemory.MainActivity;
import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.db.ScoreDAO;
import com.plex.jlrf.colourmemory.model.Score;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class HighScoreFragment extends Fragment implements MainActivity.HandleBackPressed {

    private View mView;
    private CardsNavigator navigator;
    private ListView mHighScoreListView;
    private ScoreDAO mScoreDAO;
    private SimpleCursorAdapter mAdapter;

    public final static String[] FROM_COLUMNS = {Score.NICKNAME_KEY, Score.SCORE_KEY};
    private final static int[] TO_IDS = {
            R.id.tv_nickname,
            R.id.tv_score_value
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScoreDAO = ScoreDAO.newInstance(this.getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            navigator = (CardsNavigator) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CardsNavigator interface");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_high_score, container, false);
        setupUI();
        return mView;
    }



    private void setupUI(){
        mHighScoreListView = (ListView)mView.findViewById(R.id.lv_scores);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_score, null,FROM_COLUMNS, TO_IDS, 0);
        mHighScoreListView.setAdapter(mAdapter);
        mAdapter.swapCursor(mScoreDAO.getScores());

    }



    @Override
    public void onBackPressed() {
        navigator.navigateToField();
    }
}
