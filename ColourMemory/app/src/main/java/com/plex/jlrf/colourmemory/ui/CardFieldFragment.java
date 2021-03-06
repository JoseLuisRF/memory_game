package com.plex.jlrf.colourmemory.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.plex.jlrf.colourmemory.MainActivity;
import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.db.ScoreDAO;
import com.plex.jlrf.colourmemory.model.Card;
import com.plex.jlrf.colourmemory.model.Score;
import com.plex.jlrf.colourmemory.ui.pattern.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jose.ramos.fernandez on 9/28/15.
 */
public class CardFieldFragment extends BaseFragment implements MainActivity.HandleBackPressed{

    //Constants
    private static final int MAX_SELECTABLE_CARDS = 2;
    private static final int FIRST_CARD_INDEX = 0;
    private static final int SECOND_CARD_INDEX = 1;
    private static final int RESET_NUM_TOUCHES = 0;
    private static final int MAX_TIME_FOR_VISIBILITY = 900;
    private static final int POINTS_FOR_MATCH = 2;
    private static final int POINTS_FOR_DISMATCH = -1;
    private static final int MAX_MATCHES_NUMBER = 8;


    //Member variables
    private CardsNavigator navigator;
    private View mView = null;
    private List<Card> mCards = null;
    private Card[] mSelectedCards = null;
    private int mNumTouches;
    private Random mRandom = null;
    private Handler mHandler = null;

    private TextView mScoreTextView = null;
    private Button mHighScoreButton = null;

    private int mScore;
    private int mMatchesNumber = 0;
    private Score mCurrentHighScore = null;
    private ScoreDAO mScoreDAO = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectedCards = new Card[MAX_SELECTABLE_CARDS];
        mCards = new ArrayList<Card>();
        mHandler = new Handler();
        mRandom = new Random();
        mScoreDAO = ScoreDAO.newInstance(this.getActivity());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try{
            navigator = (CardsNavigator)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CardsNavigator interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_card_board, container, false);
        setupView();
        createField();
        randomCards();
        return mView;
    }

    private void setupView(){
        mScoreTextView = (TextView)mView.findViewById(R.id.tv_score);
        updateScore(0);

        mHighScoreButton = (Button)mView.findViewById(R.id.btn_high_score);
        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateToHighScore();
            }
        });

    }

    private void validate() {

        if(mSelectedCards[FIRST_CARD_INDEX] == null || mSelectedCards[SECOND_CARD_INDEX] == null)
            return;

        if (mSelectedCards[FIRST_CARD_INDEX].getUpDrawResourceId() == mSelectedCards[SECOND_CARD_INDEX].getUpDrawResourceId()) {
            updateScore(POINTS_FOR_MATCH);
            mMatchesNumber++;
            for (int i = 0; i < mSelectedCards.length; i++) {
                mSelectedCards[i].removeFromSubjectListener();
                mSelectedCards[i] = null;
            }
        } else {
            updateScore(POINTS_FOR_DISMATCH);
            for (int i = 0; i < mSelectedCards.length; i++) {
                mSelectedCards[i].setChecked(false);
                mSelectedCards[i] = null;
            }

        }

        mNumTouches = RESET_NUM_TOUCHES;
        this.setEnableCards(true);
        if (mMatchesNumber == MAX_MATCHES_NUMBER) {
            int points = mScore;
            randomCards();
            if( mCurrentHighScore != null && mCurrentHighScore.getScore() > points){
                return;
            }
            navigator.showSaveScoreDialog(points);
        }

    }

    private void updateScore(int point) {
        if(point == 0){
            mScore = 0;
        } else {
            mScore += point;
        }

        mScoreTextView.setText(getString(R.string.score, String.valueOf(mScore)));
    }





    private void setCheckedListener(Card card) {

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card selectedCard = (Card) v;
                if (mNumTouches >= 0 && mNumTouches < MAX_SELECTABLE_CARDS) {
                    if (selectedCard.isChecked()) {
                        mSelectedCards[mNumTouches] = selectedCard;
                        mNumTouches++;
                    } else {
                        mSelectedCards[mNumTouches] = null;
                        mNumTouches--;
                    }
                }

                if (mNumTouches == MAX_SELECTABLE_CARDS) {
                    CardFieldFragment.this.setEnableCards(false);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            validate();
                        }
                    }, MAX_TIME_FOR_VISIBILITY);
                }


            }
        });
    }


    private void createField() {
        TableLayout tableLayout = (TableLayout)mView.findViewById(R.id.tbl_field);
        for ( int r = 0; r < tableLayout.getChildCount(); r++){
            if(tableLayout.getChildAt(r) instanceof TableRow){
                TableRow tableRow = (TableRow)tableLayout.getChildAt(r);
                for(int c = 0 ; c < tableRow.getChildCount(); c++ ){
                    if( tableRow.getChildAt(c) instanceof  Card){
                        Card card = (Card)tableRow.getChildAt(c);
                        setCheckedListener(card);
                        mCards.add(card);
                    }
                }
            }
        }
        Log.i("Cards", "mCards.size()::" + mCards.size());

    }

    private void randomCards(){
        List<Card> randomCards = new ArrayList<Card>(mCards);
        mCurrentHighScore = mScoreDAO.getHighScore();
        mMatchesNumber = 0;
        updateScore(0);

        int numCards = randomCards.size() - 1;
        int cardId = 1;
        while(numCards >= 0){
            for(int i = 0; i < 2; i++){

                int index = numCards;
                if(numCards != 0)
                    index = mRandom.nextInt(numCards);

                Card card = randomCards.remove(index);
                card.setUpDrawResourceId(getTypeCardByIndex(cardId));
                card.setChecked(false);
                card.setSubjectListener(this);

                numCards--;
            }
            cardId++;

        }


    }

    private TypeCard getTypeCardByIndex(int index){
        switch (index){
            case 1:
                return TypeCard.COLOUR_1;
            case 2:
                return TypeCard.COLOUR_2;
            case 3:
                return TypeCard.COLOUR_3;
            case 4:
                return TypeCard.COLOUR_4;
            case 5:
                return TypeCard.COLOUR_5;
            case 6:
                return TypeCard.COLOUR_6;
            case 7:
                return TypeCard.COLOUR_7;
            case 8:
                return TypeCard.COLOUR_8;


        }
        return null;
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }




    public enum TypeCard {
        COLOUR_1(R.drawable.colour1),
        COLOUR_2(R.drawable.colour2),
        COLOUR_3(R.drawable.colour3),
        COLOUR_4(R.drawable.colour4),
        COLOUR_5(R.drawable.colour5),
        COLOUR_6(R.drawable.colour6),
        COLOUR_7(R.drawable.colour7),
        COLOUR_8(R.drawable.colour8);

        private int resourceId;

        TypeCard(int r) {
            this.setResourceId(r);
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}
