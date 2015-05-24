package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    public enum ItemAttribute{
        CONSUMABLE(1),
        WEARABLE(2),
        STACKABLE(4);

        private int _attribute;

        ItemAttribute(int attribute){
            this._attribute = attribute;
        }

        public int getValue(){
            return _attribute;
        }

    }

    public enum ItemType{
        RESTORE_HEALTH(1),
        RESTORE_MP(2),
        DAMAGE(4),
        WEAPON_ONEHAND(8),
        WEAPON_TWOHAND(16),
        WAND_ONEHAND(32),
        WAND_TWOHAND(64),
        ARMOR_SHIELD(128),
        ARMOR_HELMET(256),
        ARMOR_CHEST(512),
        ARMOR_FEET(1024);

        private int _itemType;

        ItemType(int itemType){
            this._itemType = itemType;
        }

        public int getValue(){
            return _itemType;
        }
    }

    private int _itemAttributes;
    private int _itemType;
    private String _itemID;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, String itemID, int itemType){
        super(textureRegion);

        this._itemAttributes = itemAttributes;
        this._itemID = itemID;
        this._itemType = itemType;
    }

    public String getItemID() {
        return _itemID;
    }

    public int getItemType() {
        return _itemType;
    }

    public boolean isStackable(){
        return ((_itemAttributes & ItemAttribute.STACKABLE.getValue()) == ItemAttribute.STACKABLE.getValue());
    }

    public boolean isSameItemType(InventoryItem candidateInventoryItem){
        return _itemID.equalsIgnoreCase(candidateInventoryItem.getItemID());
    }
}
