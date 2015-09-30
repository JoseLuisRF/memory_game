package com.plex.jlrf.colourmemory.model;

import android.content.Context;
import android.util.AttributeSet;

import com.plex.jlrf.colourmemory.ui.CardFieldFragment.TypeCard;
import com.plex.jlrf.colourmemory.R;
import com.plex.jlrf.colourmemory.ui.pattern.BaseFragment;
import com.plex.jlrf.colourmemory.ui.pattern.Observer;

/**
 * Created by jose.ramos.fernandez on 9/28/15.
 */
public class Card extends Observer {

    private TypeCard upDrawResourceId;

    public Card(Context context) {
        super(context);
        init();
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void update(boolean state) {
        this.setEnabled(state);
    }

    public void setSubjectListener(BaseFragment baseFragment){
        this.subject = baseFragment;
        this.subject.attach(this);
    }

    public void removeFromSubjectListener(){
        this.subject.dettach(this);
    }
    private void init() {
        flipDown();
        this.setTextOff(null);
        this.setTextOn(null);
        this.setText(null);

    }

    public void flipUp(){
        setChecked(true);
        this.setBackgroundResource(upDrawResourceId.getResourceId());
    }

    public void flipDown(){
        setChecked(false);
        this.setBackgroundResource(R.drawable.card_bg);
    }

    public TypeCard getUpDrawResourceId() {
        return upDrawResourceId;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if( checked ){
            this.setEnabled(false);
            this.setBackgroundResource(upDrawResourceId.getResourceId());
        } else {
            this.setEnabled(true);
            this.setBackgroundResource(R.drawable.card_bg);
        }
    }

    public void setUpDrawResourceId(TypeCard upDrawResourceId) {
        this.upDrawResourceId = upDrawResourceId;
    }
}
