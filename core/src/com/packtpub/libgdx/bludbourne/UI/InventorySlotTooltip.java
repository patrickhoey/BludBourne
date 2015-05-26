package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class InventorySlotTooltip extends Window {

    private InventorySlot _inventorySlot;
    private Skin _skin;
    private Label _description;

    public InventorySlotTooltip(InventorySlot inventorySlot, final Skin skin){
        super("", skin);

        this._inventorySlot = inventorySlot;
        this._skin = skin;

        _description = new Label("", skin, "inventory-item-count");

        this.add(_description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }


    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if( _inventorySlot == null ){
            return;
        }

        if (!_inventorySlot.hasItem()) {
            super.setVisible(false);
        }
    }

    public void updateDescription(){
        if( _inventorySlot.hasItem() ){
            _description.setText(_inventorySlot.getTopInventoryItem().getItemShortDescription());
            this.pack();
        }else{
            _description.setText("");
        }

    }

}
