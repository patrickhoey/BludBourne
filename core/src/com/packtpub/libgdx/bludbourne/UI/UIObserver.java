package com.packtpub.libgdx.bludbourne.UI;

public interface UIObserver {
    public static enum UIEvent{
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION
    }

    void onNotify(final String value, UIEvent event);
}
