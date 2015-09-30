package com.plex.jlrf.colourmemory.ui.pattern;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose.ramos.fernandez on 9/30/15.
 */
public class BaseFragment extends Fragment{
    private List<Observer> observers = new ArrayList<Observer>();
    private boolean enableState;

    public boolean getState() {
        return enableState;
    }

    public void setEnableCards(boolean state) {
        this.enableState = state;
        notifyAllObservers();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }
    public void dettach(Observer observer){
        observers.remove(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update(this.enableState);
        }
    }
}
