package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventorySlotItem extends Image {

    public static final int CONSUMABLE =    0x01;
    public static final int WEARABLE =      0x02;
    public static final int STACKABLE =     0x04;

    private int _itemAttributes;
    private String _itemID;

    public InventorySlotItem(TextureRegion textureRegion, int itemAttributes, String itemID){
        super(textureRegion);

        _itemAttributes = itemAttributes;
        _itemID = itemID;
    }

    public int getItemAttributes() {
        return _itemAttributes;
    }

    public void setItemAttributes(int itemAttributes) {
        this._itemAttributes = itemAttributes;
    }

    public String getItemID() {
        return _itemID;
    }

    public void setItemID(String itemID) {
        this._itemID = itemID;
    }
}
