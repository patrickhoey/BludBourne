package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    public static final int CONSUMABLE =    0x01;
    public static final int WEARABLE =      0x02;
    public static final int STACKABLE =     0x04;

    private int _itemAttributes;
    private String _itemID;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, String itemID){
        super(textureRegion);

        _itemAttributes = itemAttributes;
        _itemID = itemID;
    }

    public String getItemID() {
        return _itemID;
    }

    public boolean isStackable(){
        return ((_itemAttributes & InventoryItem.STACKABLE) == InventoryItem.STACKABLE);
    }

    public boolean isSameItemType(InventoryItem candidateInventoryItem){
        return _itemID.equalsIgnoreCase(candidateInventoryItem.getItemID());
    }
}
