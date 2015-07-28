package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
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
        super.setDrawable(new TextureRegionDrawable(animation.getKeyFrame(0)));
        this._animation = animation;
    }

    @Override
    public void act(float delta){
        Drawable drawable = this.getDrawable();
        if( drawable == null ) return;
        _frameTime = (_frameTime + delta)%5;
        ((TextureRegionDrawable)drawable).setRegion(_animation.getKeyFrame(_frameTime, true));
        super.act(delta);
    }


}
