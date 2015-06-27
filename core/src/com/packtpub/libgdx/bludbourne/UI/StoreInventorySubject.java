package com.packtpub.libgdx.bludbourne.UI;


public interface StoreInventorySubject {
    public void addObserver(StoreInventoryObserver storeObserver);
    public void removeObserver(StoreInventoryObserver storeObserver);
    public void removeAllObservers();
    public void notify(String value, StoreInventoryObserver.StoreInventoryEvent event);
}
