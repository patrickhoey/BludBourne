package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;

public class InventorySlotTarget extends Target {

    InventorySlot _targetSlot;

    public InventorySlotTarget(InventorySlot actor){
        super(actor);
        _targetSlot = actor;
    }

    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void reset(Source source, Payload payload) {
    }

    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        InventorySlotItem sourceActor = (InventorySlotItem) payload.getDragActor();
        InventorySlotItem targetActor = _targetSlot.getTopInventoryItem();

        if( sourceActor == null ) {
            return;
        }

        if( !_targetSlot.hasItem() ){
            _targetSlot.add(sourceActor);
        }else{
            //If the same item and stackable, add
            if( sourceActor.getItemID().equals(targetActor.getItemID()) &&
                (sourceActor.getItemAttributes() & InventorySlotItem.STACKABLE) == InventorySlotItem.STACKABLE){
                _targetSlot.add(sourceActor);
            }else{
                ((InventorySlotSource)source)._sourceSlot.add(sourceActor);
            }
        }

    }
}
