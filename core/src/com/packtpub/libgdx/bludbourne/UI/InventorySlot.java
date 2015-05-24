package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class InventorySlot extends Stack {

    //All slots have this default image
    private Image _imageBackground;
    private Label _numItemsLabel;
    private int _numItemsVal = 0;

    public InventorySlot(){
        _imageBackground = new Image(new NinePatch(PlayerHUD.statusUITextureAtlas.createPatch("dialog")));
        this.add(_imageBackground);
        _numItemsLabel = new Label(String.valueOf(_numItemsVal), PlayerHUD.statusUISkin, "inventory-item-count");
        _numItemsLabel.setAlignment(Align.bottomRight);
        _numItemsLabel.setVisible(false);
        this.add(_numItemsLabel);
    }

    public void decrementItemCount() {
        _numItemsVal--;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        checkVisibilityOfItemCount();
    }

    public void incrementItemCount() {
        _numItemsVal++;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        checkVisibilityOfItemCount();
    }

    @Override
    public void add(Actor actor) {
        super.add(actor);

        if( _numItemsLabel == null ){
            return;
        }

        if( !actor.equals(_imageBackground) && !actor.equals(_numItemsLabel) ) {
            incrementItemCount();
        }
    }

    public void add(Array<Actor> array) {
        for( Actor actor : array){
            super.add(actor);

            if( _numItemsLabel == null ){
                return;
            }

            if( !actor.equals(_imageBackground) && !actor.equals(_numItemsLabel) ) {
                incrementItemCount();
            }
        }
    }

    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  arrayChildren.size - 2;
            for(int i = 0; i < numInventoryItems; i++) {
                items.add(arrayChildren.pop());
                decrementItemCount();
            }
        }
        return items;
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

    static public void swapSlots(InventorySlot inventorySlotSource, InventorySlot inventorySlotTarget, Actor dragActor){
        //swap
        Array<Actor> tempArray = inventorySlotSource.getAllInventoryItems();
        tempArray.add(dragActor);
        inventorySlotSource.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }
}
