package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ScreenTransitionActor extends Image {
    private Color _transitionColor = Color.BLACK;

    public ScreenTransitionActor(){
        init();
    }

    public ScreenTransitionActor(Color color){
        this._transitionColor = color;

        init();
    }

    private void init(){
        toFront();
        setFillParent(true);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(_transitionColor);
        pixmap.fill();
        setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
        clearListeners();
        setTouchable(Touchable.disabled);
    }

    public Color getTransitionColor() {
        return _transitionColor;
    }

    public void setTransitionColor(Color transitionColor) {
        this._transitionColor = transitionColor;
    }
}
