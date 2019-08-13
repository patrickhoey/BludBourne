package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.packtpub.libgdx.bludbourne.InventoryItem;
import com.packtpub.libgdx.bludbourne.Utility;

public class InventorySlot extends Stack implements InventorySlotSubject {

    //All slots have this default image
    private Stack _defaultBackground;
    private Image _customBackgroundDecal;
    private Label _numItemsLabel;
    private int _numItemsVal = 0;
    private int _filterItemType;

    private Array<InventorySlotObserver> _observers;

    public InventorySlot(){
        _filterItemType = 0; //filter nothing
        _defaultBackground = new Stack();
        _customBackgroundDecal = new Image();
        _observers = new Array<InventorySlotObserver>();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog")));

        _numItemsLabel = new Label(String.valueOf(_numItemsVal), Utility.STATUSUI_SKIN, "inventory-item-count");
        _numItemsLabel.setAlignment(Align.bottomRight);
        _numItemsLabel.setVisible(false);

        _defaultBackground.add(image);

        _defaultBackground.setName("background");
        _numItemsLabel.setName("numitems");

        this.add(_defaultBackground);
        this.add(_numItemsLabel);
    }

    public InventorySlot(int filterItemType, Image customBackgroundDecal){
        this();
        _filterItemType = filterItemType;
        _customBackgroundDecal = customBackgroundDecal;
        _defaultBackground.add(_customBackgroundDecal);
    }

    public void decrementItemCount(boolean sendRemoveNotification) {
        _numItemsVal--;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if( _defaultBackground.getChildren().size == 1 ){
            _defaultBackground.add(_customBackgroundDecal);
        }
        checkVisibilityOfItemCount();
        if( sendRemoveNotification ){
            notify(this, InventorySlotObserver.SlotEvent.REMOVED_ITEM);
        }

    }

    public void incrementItemCount(boolean sendAddNotification) {
        _numItemsVal++;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if( _defaultBackground.getChildren().size > 1 ){
            _defaultBackground.getChildren().pop();
        }
        checkVisibilityOfItemCount();
        if( sendAddNotification ){
            notify(this, InventorySlotObserver.SlotEvent.ADDED_ITEM);
        }
    }

    @Override
    public void add(Actor actor) {
        super.add(actor);

        if( _numItemsLabel == null ){
            return;
        }

        if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
            incrementItemCount(true);
        }
    }

    public void remove(Actor actor) {
        super.removeActor(actor);

        if( _numItemsLabel == null ){
            return;
        }

        if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
            decrementItemCount(true);
        }
    }

    public void add(Array<Actor> array) {
        for( Actor actor : array){
            super.add(actor);

            if( _numItemsLabel == null ){
                return;
            }

            if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
                incrementItemCount(true);
            }
        }
    }

    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  arrayChildren.size - 2;
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(true);
                items.add(arrayChildren.pop());
            }
        }
        return items;
    }

    public void updateAllInventoryItemNames(String name){
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for(int i = arrayChildren.size - 1; i > 1 ; i--) {
                arrayChildren.get(i).setName(name);
            }
        }
    }

    public void removeAllInventoryItemsWithName(String name){
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for(int i = arrayChildren.size - 1; i > 1 ; i--) {
                String itemName = arrayChildren.get(i).getName();
                if( itemName.equalsIgnoreCase(name)){
                    decrementItemCount(true);
                    arrayChildren.removeIndex(i);
                }
            }
        }
    }


    public void clearAllInventoryItems(boolean sendRemoveNotifications) {
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  getNumItems();
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(sendRemoveNotifications);
                arrayChildren.pop();
            }
        }
    }

    private void checkVisibilityOfItemCount(){
        if( _numItemsVal < 2){
            _numItemsLabel.setVisible(false);
        }else{
            _numItemsLabel.setVisible(true);
        }
    }

    public boolean hasItem(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                return true;
            }
        }
        return false;
    }

    public int getNumItems(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 2;
        }
        return 0;
    }

    public int getNumItems(String name){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            int totalFilteredSize = 0;
            for( Actor actor: items ){
                if( actor.getName().equalsIgnoreCase(name)){
                    totalFilteredSize++;
                }
            }
            return totalFilteredSize;
        }
        return 0;
    }

    public boolean doesAcceptItemUseType(int itemUseType){
        if( _filterItemType == 0 ){
            return true;
        }else {
            return ((_filterItemType & itemUseType) == itemUseType);
        }
    }

    public InventoryItem getTopInventoryItem(){
        InventoryItem actor = null;
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }

    static public void swapSlots(InventorySlot inventorySlotSource, InventorySlot inventorySlotTarget, InventoryItem dragActor){
        //check if items can accept each other, otherwise, no swap
        if( !inventorySlotTarget.doesAcceptItemUseType(dragActor.getItemUseType()) ||
                !inventorySlotSource.doesAcceptItemUseType(inventorySlotTarget.getTopInventoryItem().getItemUseType())) {
            inventorySlotSource.add(dragActor);
            return;
        }

        //swap
        Array<Actor> tempArray = inventorySlotSource.getAllInventoryItems();
        tempArray.add(dragActor);
        inventorySlotSource.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }

    @Override
    public void addObserver(InventorySlotObserver slotObserver) {
        _observers.add(slotObserver);
    }

    @Override
    public void removeObserver(InventorySlotObserver slotObserver) {
        _observers.removeValue(slotObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for(InventorySlotObserver observer: _observers){
            _observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event) {
        for(InventorySlotObserver observer: _observers){
            observer.onNotify(slot, event);
        }
    }
}
