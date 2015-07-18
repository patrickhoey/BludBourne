package com.packtpub.libgdx.bludbourne;

public interface ComponentObserver {
    public static enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION,
        QUEST_LOCATION_DISCOVERED
    }

    void onNotify(final String value, ComponentEvent event);
}
