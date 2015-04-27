package com.packtpub.libgdx.bludbourne;


public interface Component {

    public static class MESSAGE {
        public static final String MESSAGE_TOKEN = ":::::";
        public static final String CURRENT_POSITION = "currentPosition";
        public static final String INIT_START_POSITION = "initStartPosition";
    }

    void dispose();
    void receive(String message);
}
