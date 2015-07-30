package com.packtpub.libgdx.bludbourne.UI;

public interface InventoryObserver {
    public static enum InventoryEvent {
        UPDATED_AP,
        UPDATED_DP,
        NONE
    }

    void onNotify(final String value, InventoryEvent event);
}
