package com.packtpub.libgdx.bludbourne.dialog;

public interface ConversationGraphObserver {
    public static enum ConversationCommandEvent {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        ACCEPT_QUEST,
        ADD_ENTITY_TO_INVENTORY,
        RETURN_QUEST,
        NONE
    }

    void onNotify(final ConversationGraph graph, ConversationCommandEvent event);
}
