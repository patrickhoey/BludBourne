package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class ScreenTransitionAction extends Action {
    public static enum ScreenTransitionType{
        FADE_IN,
        FADE_OUT,
        NONE
    }

    private ScreenTransitionType _transitionType = ScreenTransitionType.NONE;
    private float _transitionDuration = 3;

    public ScreenTransitionAction(){
    }

    public ScreenTransitionAction(ScreenTransitionType type, float duration){
        this._transitionType = type;
        this._transitionDuration = duration;
    }

    @Override
    public boolean act(float delta) {
        Actor actor = getTarget();
        if ( actor == null ) return false;
        switch(_transitionType){
            case FADE_IN:
                SequenceAction fadeIn = Actions.sequence(
                        Actions.alpha(1),
                        Actions.fadeOut(_transitionDuration));
                actor.addAction(fadeIn);
                break;
            case FADE_OUT:
                SequenceAction fadeOut = Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(_transitionDuration));
                actor.addAction(fadeOut);
                break;
            case NONE:
                break;
            default:
                break;
        }
        return true;
    }

    public static ScreenTransitionAction transition (ScreenTransitionType type, float duration) {
        ScreenTransitionAction action = Actions.action(ScreenTransitionAction.class);
        action.setTransitionType(type);
        action.setTransitionDuration(duration);
        return action;
    }

    public ScreenTransitionType getTransitionType() {
        return _transitionType;
    }

    public void setTransitionType(ScreenTransitionType transitionType) {
        this._transitionType = transitionType;
    }

    public float getTransitionDuration() {
        return _transitionDuration;
    }

    public void setTransitionDuration(float transitionDuration) {
        this._transitionDuration = transitionDuration;
    }
}
