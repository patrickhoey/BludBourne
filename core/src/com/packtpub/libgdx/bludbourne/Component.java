package com.packtpub.libgdx.bludbourne;


public interface Component {

    public static final String MESSAGE_TOKEN = ":::::";

    public static enum MESSAGE{
        CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE
    }

    void dispose();
    void receiveMessage(String message);
}
