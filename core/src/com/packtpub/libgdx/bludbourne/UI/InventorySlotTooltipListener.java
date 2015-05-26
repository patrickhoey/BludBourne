package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class InventorySlotTooltipListener extends InputListener {

    private InventorySlotTooltip _toolTip;
    private boolean _isInside = false;
    private Vector2 _currentCoords;
    private Vector2 _offset;

    public InventorySlotTooltipListener(InventorySlotTooltip toolTip){
        this._toolTip = toolTip;
        this._currentCoords = new Vector2(0,0);
        this._offset = new Vector2(20, 10);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        if( _isInside ){
            _currentCoords.set(x, y);
            inventorySlot.localToStageCoordinates(_currentCoords);

            _toolTip.setPosition(_currentCoords.x+_offset.x, _currentCoords.y+_offset.y);
        }
        return false;
    }


    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        _toolTip.setVisible(inventorySlot, false);
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();

        _isInside = true;

        _currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(_currentCoords);

        _toolTip.updateDescription(inventorySlot);
        _toolTip.setPosition(_currentCoords.x + _offset.x, _currentCoords.y + _offset.y);
        _toolTip.toFront();
        _toolTip.setVisible(inventorySlot, true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        _toolTip.setVisible(inventorySlot, false);
        _isInside = false;

        _currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(_currentCoords);
    }

}
