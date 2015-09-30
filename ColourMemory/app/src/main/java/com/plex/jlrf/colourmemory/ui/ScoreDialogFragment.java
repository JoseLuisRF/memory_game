package com.plex.jlrf.colourmemory.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.db.ScoreDAO;
import com.plex.jlrf.colourmemory.model.Score;

/**
 * Created by jose.ramos.fernandez on 9/29/15.
 */
public class ScoreDialogFragment extends DialogFragment {

    private static final String SCORE_KEY = "SCORE_KEY";
    private static ScoreDialogFragment mInstance = null;
    private Button mSaveButton;
    private Button mCancelButton;
    private EditText mNicknameEditText;
    private View mView;
    private ScoreDAO mScoreDAO;
    private int mScore = 0;

    public static ScoreDialogFragment newInstance(int score) {
        if (mInstance == null) {
            mInstance = new ScoreDialogFragment();
        }
        Bundle args = new Bundle();
        args.putInt(SCORE_KEY, score);
        mInstance.setArguments(args);
        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        mScoreDAO = ScoreDAO.newInstance(mInstance.getActivity());
        if(getArguments() != null && getArguments().containsKey(SCORE_KEY)){
            mScore = getArguments().getInt(SCORE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        mView = inflater.inflate(R.layout.dialog_fragment_save_score, container, false);
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        setupView();
        setupListener();
        return mView;
    }

    private void setupView() {
        mSaveButton = (Button)mView.findViewById(R.id.btn_save_score);
        mCancelButton = (Button)mView.findViewById(R.id.btn_cancel_save_score);
        mNicknameEditText = (EditText)mView.findViewById(R.id.tv_nickname);
    }

    private void setupListener(){
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mNicknameEditText.getText())){
                    return;
                }
                Score score = new Score(mNicknameEditText.getText().toString(),mScore);
                long res = mScoreDAO.inertScore(score);
                if(res > 0){
                    ScoreDialogFragment.this.dismiss();
                }
            }
        });
    }



}
