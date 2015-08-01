package com.packtpub.libgdx.bludbourne.UI;

public interface InventoryObserver {
    public static enum InventoryEvent {
        UPDATED_AP,
        UPDATED_DP,
        ITEM_CONSUMED,
        NONE
    }

    void onNotify(final String value, InventoryEvent event);
}
