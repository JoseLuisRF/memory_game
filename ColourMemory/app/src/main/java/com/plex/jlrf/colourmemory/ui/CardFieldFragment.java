package com.plex.jlrf.colourmemory.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plex.jlrf.colourmemory.MainActivity;
import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.db.ScoreDAO;
import com.plex.jlrf.colourmemory.model.Card;
import com.plex.jlrf.colourmemory.model.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jose.ramos.fernandez on 9/28/15.
 */
public class CardFieldFragment extends Fragment implements MainActivity.HandleBackPressed{

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
    private View mBlockerView;

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
        mBlockerView = mView.findViewById(R.id.fl_blocker);
        mBlockerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:do nothing
            }
        });

        mHighScoreButton = (Button)mView.findViewById(R.id.btn_high_score);
        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                navigator.showSaveScoreDialog(mScore);
//                navigator.navigateToHighScore();
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
//                mSelectedCards[i].setEnabled(false);
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
        mBlockerView.setVisibility(View.INVISIBLE);

        if (mMatchesNumber == MAX_MATCHES_NUMBER) {
            if( mCurrentHighScore != null && mCurrentHighScore.getScore() > mScore){
                randomCards();
                return;
            }
            navigator.showSaveScoreDialog(mScore);
        }

    }

    private void updateScore(int point) {
        mScore += point;
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

                if(mNumTouches == MAX_SELECTABLE_CARDS) {
                    mBlockerView.setVisibility(View.VISIBLE);
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

    private LinearLayout createRow(ViewGroup container) {
        LinearLayout ll = new LinearLayout(this.getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(ll);
        return ll;
    }

    private void createField() {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout container = (LinearLayout) mView.findViewById(R.id.container);
        for (int i = 0; i < 4; i++) {
            LinearLayout ll = createRow(container);
            for (int j = 0; j < 4; j++) {
                Card card = (Card)inflater.inflate(R.layout.view_card_template, null);
                setCheckedListener(card);
                ll.addView(card);
                mCards.add(card);
            }
        }

    }

    private void randomCards(){
        List<Card> randomCards = new ArrayList<Card>(mCards);
        mCurrentHighScore = mScoreDAO.getHighScore();
        mMatchesNumber = 0;

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
