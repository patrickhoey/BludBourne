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

        _currentCoords = new Vector2(0,0);
        _offset = new Vector2(20, 10);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y){
        if( _isInside ){
            _currentCoords.set(x,y);
            event.getListenerActor().localToStageCoordinates(_currentCoords);
            _toolTip.setPosition(_currentCoords.x+_offset.x, _currentCoords.y+_offset.y);
        }else{
            _toolTip.setVisible(false);
        }
        return false;
    }

    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        _toolTip.setVisible(false);
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
        _toolTip.updateDescription();
        _isInside = true;
        _currentCoords.set(x, y);

        event.getListenerActor().localToStageCoordinates(_currentCoords);

        _toolTip.setPosition(_currentCoords.x + _offset.x, _currentCoords.y+_offset.y);
        _toolTip.toFront();
        _toolTip.setVisible(true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
        _toolTip.setVisible(false);
        _isInside = false;
        _currentCoords.set(x, y);
        event.getListenerActor().localToStageCoordinates(_currentCoords);
    }

}
