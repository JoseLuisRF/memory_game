package com.plex.jlrf.colourmemory.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.plex.jlrf.colourmemory.ui.CardFieldFragment.TypeCard;
import com.plex.jlrf.colourmemory.R;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by jose.ramos.fernandez on 9/28/15.
 */
public class Card extends ToggleButton {

    private TypeCard upDrawResourceId;

    public Card(Context context) {
        super(context);
        init();
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
