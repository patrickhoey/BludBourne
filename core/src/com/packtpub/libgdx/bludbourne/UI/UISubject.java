package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.utils.Array;

public class UISubject {
    private Array<UIObserver> _observers;

    public UISubject(){
        _observers = new Array<UIObserver>();
    }

    public void addObserver(UIObserver conversationObserver){
        _observers.add(conversationObserver);
    }

    public void removeObserver(UIObserver conversationObserver){
        _observers.removeValue(conversationObserver, true);
    }

    public void removeAllObservers(){
        for(UIObserver observer: _observers){
            _observers.removeValue(observer, true);
        }
    }

    protected void notify(final String value, UIObserver.UIEvent event){
        for(UIObserver observer: _observers){
            observer.onNotify(value, event);
        }
    }
}
