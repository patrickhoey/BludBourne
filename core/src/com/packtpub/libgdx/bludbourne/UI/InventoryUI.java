package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Scaling;

public class InventoryUI extends Window {

    private int _numSlots = 50;
    private int _lengthSlotRow = 10;
    private DragAndDrop _dragAndDrop;

    private final int _slotWidth = 52;
    private final int _slotHeight = 52;

    public InventoryUI(final Skin skin, TextureAtlas textureAtlas){
        super("Inventory", skin, "solidbackground");

        _dragAndDrop = new DragAndDrop();

        //create
        Table inventorySlotTable = new Table();
        Table playerSlotsTable = new Table();
        Table equipSlots = new Table();
        equipSlots.defaults().space(10);

        InventorySlot headSlot = new InventorySlot(
                InventoryItem.ItemType.ARMOR_HELMET.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_helmet")));
        InventorySlot leftArmSlot = new InventorySlot(
                InventoryItem.ItemType.WEAPON_ONEHAND.getValue() |
                InventoryItem.ItemType.WEAPON_TWOHAND.getValue() |
                InventoryItem.ItemType.ARMOR_SHIELD.getValue() |
                InventoryItem.ItemType.WAND_ONEHAND.getValue() |
                InventoryItem.ItemType.WAND_TWOHAND.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_weapon"))
        );
        InventorySlot rightArmSlot = new InventorySlot(
                InventoryItem.ItemType.WEAPON_ONEHAND.getValue() |
                InventoryItem.ItemType.WEAPON_TWOHAND.getValue() |
                InventoryItem.ItemType.ARMOR_SHIELD.getValue() |
                InventoryItem.ItemType.WAND_ONEHAND.getValue() |
                InventoryItem.ItemType.WAND_TWOHAND.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_shield"))
        );
        InventorySlot chestSlot = new InventorySlot(
                InventoryItem.ItemType.ARMOR_CHEST.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_chest")));
        InventorySlot legsSlot = new InventorySlot(
                InventoryItem.ItemType.ARMOR_FEET.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_boot")));

        _dragAndDrop.addTarget(new InventorySlotTarget(headSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(leftArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(rightArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(legsSlot));

        playerSlotsTable.setBackground(new Image(new NinePatch(textureAtlas.createPatch("dialog"))).getDrawable());

        //layout
        for(int i = 1; i <= _numSlots; i++){
            InventorySlot inventorySlot = new InventorySlot();
            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            if( i==5 || i == 10 || i == 15 || i == 20) {
                //TEMP TODO
                final InventoryItem inventorySlotItem = new InventoryItem(
                        PlayerHUD.itemsTextureAtlas.findRegion("armor01"),
                        InventoryItem.ItemAttribute.WEARABLE.getValue(),
                        "armor01",
                        InventoryItem.ItemType.ARMOR_CHEST.getValue());

                inventorySlotItem.setScaling(Scaling.none);
                inventorySlot.add(inventorySlotItem);

                _dragAndDrop.addSource(new InventorySlotSource(inventorySlot, _dragAndDrop));
            } else if( i==1 || i == 13 || i == 25 || i == 30) {
                //TEMP TODO
                final InventoryItem inventorySlotItem = new InventoryItem(
                        PlayerHUD.itemsTextureAtlas.findRegion("potions02"),
                        InventoryItem.ItemAttribute.CONSUMABLE.getValue() | InventoryItem.ItemAttribute.STACKABLE.getValue(),
                        "potions02",
                        InventoryItem.ItemType.RESTORE_MP.getValue());
                inventorySlotItem.setScaling(Scaling.none);
                inventorySlot.add(inventorySlotItem);

                _dragAndDrop.addSource(new InventorySlotSource(inventorySlot, _dragAndDrop));
            }

            inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            //inventorySlotTable.add(image);
            if(i % _lengthSlotRow == 0){
                inventorySlotTable.row();
            }
        }

        equipSlots.add();
        equipSlots.add(headSlot).size(_slotWidth, _slotHeight);
        equipSlots.row();

        equipSlots.add(leftArmSlot).size(_slotWidth, _slotHeight);
        equipSlots.add(chestSlot).size(_slotWidth, _slotHeight);
        equipSlots.add(rightArmSlot).size(_slotWidth, _slotHeight);
        equipSlots.row();

        equipSlots.add();
        equipSlots.right().add(legsSlot).size(_slotWidth, _slotHeight);

        playerSlotsTable.add(equipSlots);

        this.add(playerSlotsTable).padBottom(20).row();
        this.add(inventorySlotTable).row();
        this.pack();
    }
}
