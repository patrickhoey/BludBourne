package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.packtpub.libgdx.bludbourne.Entity;

public class AnimatedImage extends Image {
    private static final String TAG = AnimatedImage.class.getSimpleName();
    private float _frameTime = 0;
    protected Entity _entity;
    private Entity.AnimationType _currentAnimationType = Entity.AnimationType.IDLE;

    public AnimatedImage(){
        super();
    }

    public void setEntity(Entity entity){
        this._entity = entity;
        //set default
        setCurrentAnimation(Entity.AnimationType.IDLE);
    }

    public void setCurrentAnimation(Entity.AnimationType animationType){
        Animation<TextureRegion> animation = _entity.getAnimation(animationType);
        if( animation == null ){
            Gdx.app.debug(TAG, "Animation type " + animationType.toString() + " does not exist!");
            return;
        }

        this._currentAnimationType = animationType;
        this.setDrawable(new TextureRegionDrawable(animation.getKeyFrame(0)));
        this.setScaling(Scaling.stretch);
        this.setAlign(Align.center);
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }

    @Override
    public void act(float delta){
        Drawable drawable = this.getDrawable();
        if( drawable == null ) {
            //Gdx.app.debug(TAG, "Drawable is NULL!");
            return;
        }
        _frameTime = (_frameTime + delta)%5;
        TextureRegion region = _entity.getAnimation(_currentAnimationType).getKeyFrame(_frameTime, true);
        //Gdx.app.debug(TAG, "Keyframe number is " + _animation.getKeyFrameIndex(_frameTime));
        ((TextureRegionDrawable) drawable).setRegion(region);
        super.act(delta);
    }




}
