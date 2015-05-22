package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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

    private void checkVisibilityOfItemCount(){
        if( _numItemsVal < 2){
            _numItemsLabel.setVisible(false);
        }else{
            _numItemsLabel.setVisible(true);
        }
    }

    public Actor getTopInventoryItem(){
        Actor actor = null;
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 1 ){
                actor = items.peek();
            }
        }
        return actor;
    }
}
