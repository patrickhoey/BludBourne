package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class AnimatedImage extends Image {
    private static final String TAG = AnimatedImage.class.getSimpleName();

    protected Animation _animation = null;
    private float _frameTime = 0;

    public AnimatedImage(){
        super();
    }

    public AnimatedImage(Animation animation){
        super(animation.getKeyFrame(0));
        this._animation = animation;
    }

    public void setAnimation(Animation animation){
        this.setDrawable(new TextureRegionDrawable(animation.getKeyFrame(0)));
        this.setScaling(Scaling.stretch);
        this.setAlign(Align.center);
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
        this._animation = animation;
    }

    @Override
    public void act(float delta){
        Drawable drawable = this.getDrawable();
        if( drawable == null ) {
            //Gdx.app.debug(TAG, "Drawable is NULL!");
            return;
        }
        _frameTime = (_frameTime + delta)%5;
        TextureRegion region = _animation.getKeyFrame(_frameTime, true);
        //Gdx.app.debug(TAG, "Keyframe number is " + _animation.getKeyFrameIndex(_frameTime));
        ((TextureRegionDrawable) drawable).setRegion(region);
        super.act(delta);
    }




}
