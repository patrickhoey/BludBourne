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
        Actor actor = payload.getDragActor();

        if( actor == null ) {
            return;
        }
        _targetSlot.add(actor);
    }
}
