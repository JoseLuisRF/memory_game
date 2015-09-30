package com.plex.jlrf.colourmemory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.plex.jlrf.colourmemory.ui.CardFieldFragment;
import com.plex.jlrf.colourmemory.ui.CardsNavigator;
import com.plex.jlrf.colourmemory.ui.HighScoreFragment;
import com.plex.jlrf.colourmemory.ui.ScoreDialogFragment;

public class MainActivity extends AppCompatActivity implements CardsNavigator {

    private FragmentManager mFragmentManager;
    private Fragment mFragment = null;
    private HandleBackPressed mHandleBackPressed;

    public interface HandleBackPressed {
        void onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        navigateToField();


    }

    @Override
    public void navigateToField() {
        mFragment = new CardFieldFragment();
        mHandleBackPressed = (HandleBackPressed) mFragment;
        mFragmentManager.popBackStack();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, mFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void navigateToHighScore() {
        mFragment = new HighScoreFragment();
        mHandleBackPressed = (HandleBackPressed) mFragment;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, mFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showSaveScoreDialog(int score) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment prev = mFragmentManager.findFragmentByTag(ScoreDialogFragment.class.getSimpleName());
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        // Create and show the dialog.
        ScoreDialogFragment newFragment = ScoreDialogFragment.newInstance(score);

        newFragment.show(fragmentTransaction, ScoreDialogFragment.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        mHandleBackPressed.onBackPressed();
    }
}
