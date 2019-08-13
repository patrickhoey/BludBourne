package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.packtpub.libgdx.bludbourne.InventoryItem;

public class InventorySlotTooltip extends Window {

    private Skin _skin;
    private Label _description;

    public InventorySlotTooltip(final Skin skin){
        super("", skin);
        this._skin = skin;

        _description = new Label("", skin, "inventory-item-count");

        this.add(_description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }

    public void setVisible(InventorySlot inventorySlot, boolean visible) {
        super.setVisible(visible);

        if( inventorySlot == null ){
            return;
        }

        if (!inventorySlot.hasItem()) {
            super.setVisible(false);
        }
    }

    public void updateDescription(InventorySlot inventorySlot){
        if( inventorySlot.hasItem() ){
            StringBuilder string = new StringBuilder();
            InventoryItem item = inventorySlot.getTopInventoryItem();
            string.append(item.getItemShortDescription());
            if( item.isInventoryItemOffensive() ){
                string.append(System.getProperty("line.separator"));
                string.append(String.format("Attack Points: %s", item.getItemUseTypeValue()));
            }else if( item.isInventoryItemDefensive() ){
                string.append(System.getProperty("line.separator"));
                string.append(String.format("Defense Points: %s", item.getItemUseTypeValue()));
            }
            string.append(System.getProperty("line.separator"));
            string.append(String.format("Original Value: %s GP", item.getItemValue()));
            string.append(System.getProperty("line.separator"));
            string.append(String.format("Trade Value: %s GP", item.getTradeValue()));

            _description.setText(string);
            this.pack();
        }else{
            _description.setText("");
            this.pack();
        }

    }
}
