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

    public InventoryUI(final Skin skin, TextureAtlas textureAtlas){
        super("Inventory", skin, "solidbackground");
        this.setFillParent(true);

        _dragAndDrop = new DragAndDrop();

        //create
        final Table inventorySlotTable = new Table();
        Table playerSlotsTable = new Table();

        //layout
        for(int i = 1; i <= _numSlots; i++){
            InventorySlot inventorySlot = new InventorySlot();
            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            if( i==5 || i == 10 ) {
                //TEMP TODO
                final Image image = new Image(PlayerHUD.itemsTextureAtlas.findRegion("armor01"));
                image.setScaling(Scaling.none);
                inventorySlot.add(image);

                _dragAndDrop.addSource(new InventorySlotSource(inventorySlot, _dragAndDrop));
            }

            inventorySlotTable.add(inventorySlot).size(52, 52);

            //inventorySlotTable.add(image);
            if(i % _lengthSlotRow == 0){
                inventorySlotTable.row();
            }
        }

        playerSlotsTable.add(new Image(new NinePatch(textureAtlas.createPatch("dialog")))).size(200, 200);

        this.add(playerSlotsTable).padBottom(20).row();
        this.add(inventorySlotTable).row();
        this.pack();
    }
}
