package com.packtpub.libgdx.bludbourne;

public interface ComponentObserver {
    public static enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION
    }

    void onNotify(final String value, ComponentEvent event);
}
