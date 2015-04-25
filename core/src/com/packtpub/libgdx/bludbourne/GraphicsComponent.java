package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GraphicsComponent {

    private static final String TAG = GraphicsComponent.class.getSimpleName();

    private static final String _defaultSpritePath = "sprites/characters/Warrior.png";

    //private Direction _currentDirection = Direction.LEFT;
    //private Direction _previousDirection = Direction.UP;

    private Array<TextureRegion> _walkLeftFrames;
    private Array<TextureRegion> _walkRightFrames;
    private Array<TextureRegion> _walkUpFrames;
    private Array<TextureRegion> _walkDownFrames;

    private Animation _walkLeftAnimation;
    private Animation _walkRightAnimation;
    private Animation _walkUpAnimation;
    private Animation _walkDownAnimation;

    protected float _frameTime = 0f;
    protected Sprite _frameSprite = null;
    protected TextureRegion _currentFrame = null;

    public GraphicsComponent(){
        Utility.loadTextureAsset(_defaultSpritePath);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(Batch batch, Entity entity, float delta){
        _frameTime = (_frameTime + delta)%5; //Want to avoid overflow

        //Look into the appropriate variable when changing position
        switch (entity._direction) {
            case DOWN:
                if (entity._state == Entity.State.WALKING) {
                    _currentFrame = _walkDownAnimation.getKeyFrame(_frameTime);
                } else {
                    _currentFrame = _walkDownAnimation.getKeyFrames()[0];
                }
                break;
            case LEFT:
                if (entity._state == Entity.State.WALKING) {
                    _currentFrame = _walkLeftAnimation.getKeyFrame(_frameTime);
                }else{
                    _currentFrame = _walkLeftAnimation.getKeyFrames()[0];
                }
                break;
            case UP:
                if (entity._state == Entity.State.WALKING) {
                    _currentFrame = _walkUpAnimation.getKeyFrame(_frameTime);
                }else{
                    _currentFrame = _walkUpAnimation.getKeyFrames()[0];
                }
                break;
            case RIGHT:
                if (entity._state == Entity.State.WALKING) {
                    _currentFrame = _walkRightAnimation.getKeyFrame(_frameTime);
                }else{
                    _currentFrame = _walkRightAnimation.getKeyFrames()[0];
                }
                break;
            default:
                break;
        }

        batch.begin();
        batch.draw(_currentFrame, entity._currentPlayerPosition.x, entity._currentPlayerPosition.y, 1, 1);
        batch.end();
    }

    public void dispose(){
        Utility.unloadAsset(_defaultSpritePath);
    }

    private void loadDefaultSprite()
    {
        Texture texture = Utility.getTextureAsset(_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        _frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0,0,Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        _currentFrame = textureFrames[0][0];
    }

    private void loadAllAnimations(){
        //Walking animation
        Texture texture = Utility.getTextureAsset(_defaultSpritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        _walkDownFrames = new Array<TextureRegion>(4);
        _walkLeftFrames = new Array<TextureRegion>(4);
        _walkRightFrames = new Array<TextureRegion>(4);
        _walkUpFrames = new Array<TextureRegion>(4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //Gdx.app.debug(TAG, "Got frame " + i + "," + j + " from " + sourceImage);
                TextureRegion region = textureFrames[i][j];
                if( region == null ){
                    Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
                }
                switch(i)
                {
                    case 0:
                        _walkDownFrames.insert(j, region);
                        break;
                    case 1:
                        _walkLeftFrames.insert(j, region);
                        break;
                    case 2:
                        _walkRightFrames.insert(j, region);
                        break;
                    case 3:
                        _walkUpFrames.insert(j, region);
                        break;
                }
            }
        }


        _walkDownAnimation = new Animation(0.25f, _walkDownFrames, Animation.PlayMode.LOOP);
        _walkLeftAnimation = new Animation(0.25f, _walkLeftFrames, Animation.PlayMode.LOOP);
        _walkRightAnimation = new Animation(0.25f, _walkRightFrames, Animation.PlayMode.LOOP);
        _walkUpAnimation = new Animation(0.25f, _walkUpFrames, Animation.PlayMode.LOOP);
    }

}
