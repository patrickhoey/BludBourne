package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    public enum ItemAttribute{
        CONSUMABLE(1),
        EQUIPPABLE(2),
        STACKABLE(4);

        private int _attribute;

        ItemAttribute(int attribute){
            this._attribute = attribute;
        }

        public int getValue(){
            return _attribute;
        }

    }

    public enum ItemUseType{
        ITEM_RESTORE_HEALTH(1),
        ITEM_RESTORE_MP(2),
        ITEM_DAMAGE(4),
        WEAPON_ONEHAND(8),
        WEAPON_TWOHAND(16),
        WAND_ONEHAND(32),
        WAND_TWOHAND(64),
        ARMOR_SHIELD(128),
        ARMOR_HELMET(256),
        ARMOR_CHEST(512),
        ARMOR_FEET(1024),
        QUEST_ITEM(2048);

        private int _itemUseType;

        ItemUseType(int itemUseType){
            this._itemUseType = itemUseType;
        }

        public int getValue(){
            return _itemUseType;
        }
    }

    public enum ItemTypeID {
        ARMOR01,ARMOR02,ARMOR03,ARMOR04,ARMOR05,
        BOOTS01,BOOTS02,BOOTS03,BOOTS04,BOOTS05,
        HELMET01,HELMET02,HELMET03,HELMET04,HELMET05,
        SHIELD01,SHIELD02,SHIELD03,SHIELD04,SHIELD05,
        WANDS01,WANDS02,WANDS03,WANDS04,WANDS05,
        WEAPON01,WEAPON02,WEAPON03,WEAPON04,WEAPON05,
        POTIONS01,POTIONS02,POTIONS03,
        SCROLL01,SCROLL02,SCROLL03,
        HERB001,BABY001,
        NONE
        ;
    }

    private int itemAttributes;
    private int itemUseType;
    private ItemTypeID itemTypeID;
    private String itemShortDescription;
    private int itemValue;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, ItemTypeID itemTypeID, int itemUseType, int itemValue){
        super(textureRegion);

        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.itemUseType = itemUseType;
        this.itemValue = itemValue;
    }

    public InventoryItem(){
        super();
    }

    public InventoryItem(InventoryItem inventoryItem){
        super();
        this.itemTypeID = inventoryItem.getItemTypeID();
        this.itemAttributes = inventoryItem.getItemAttributes();
        this.itemUseType = inventoryItem.getItemUseType();
        this.itemShortDescription = inventoryItem.getItemShortDescription();
        this.itemValue = inventoryItem.getItemValue();
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    public ItemTypeID getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(ItemTypeID itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public int getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(int itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public int getItemUseType() {
        return itemUseType;
    }

    public void setItemUseType(int itemUseType) {
        this.itemUseType = itemUseType;
    }

    public String getItemShortDescription() {
        return itemShortDescription;
    }

    public void setItemShortDescription(String itemShortDescription) {
        this.itemShortDescription = itemShortDescription;
    }

    public boolean isStackable(){
        return ((itemAttributes & ItemAttribute.STACKABLE.getValue()) == ItemAttribute.STACKABLE.getValue());
    }

    public boolean isSameItemType(InventoryItem candidateInventoryItem){
        return itemTypeID == candidateInventoryItem.getItemTypeID();
    }

    public int getTradeValue(){
        //For now, we will set the trade in value of items at about one third their original value
        if( itemValue >= 0 ) {
            return MathUtils.floor(itemValue * .33f) + 2;
        }else{
            return 0;
        }
    }
}
