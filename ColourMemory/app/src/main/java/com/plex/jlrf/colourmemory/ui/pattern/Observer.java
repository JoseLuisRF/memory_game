package com.plex.jlrf.colourmemory.ui.pattern;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Created by jose.ramos.fernandez on 9/30/15.
 */
public abstract class Observer extends ToggleButton {
    protected BaseFragment subject;

    public Observer(Context context) {
        super(context);
    }

    public Observer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Observer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public abstract void update(boolean state);


}
