package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Hashtable;

public class NPCGraphicsComponent extends GraphicsComponent {

    private static final String TAG = NPCGraphicsComponent.class.getSimpleName();

    private Vector2 _currentPosition;
    private Entity.State _currentState;
    private Entity.Direction _currentDirection;
    private ShapeRenderer _shapeRenderer;

    private Json _json;

    private float _frameTime = 0f;
    private TextureRegion _currentFrame = null;

    public NPCGraphicsComponent(){
        _currentPosition = new Vector2(0,0);
        _currentState = Entity.State.WALKING;
        _currentDirection = Entity.Direction.DOWN;
        _animations = new Hashtable<Entity.AnimationType, Animation>();
        _shapeRenderer = new ShapeRenderer();
        _json = new Json();
    }

    @Override
    public void receiveMessage(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString())) {
                _currentPosition = _json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                _currentPosition = _json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                _currentState = _json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                _currentDirection = _json.fromJson(Entity.Direction.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString())) {
                EntityConfig entityConfig = _json.fromJson(EntityConfig.class, string[1]);
                Array<EntityConfig.AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

                for( EntityConfig.AnimationConfig animationConfig : animationConfigs ){
                    Array<String> textureNames = animationConfig.getTexturePaths();
                    Array<GridPoint2> points = animationConfig.getGridPoints();
                    Entity.AnimationType animationType = animationConfig.getAnimationType();
                    float frameDuration = animationConfig.getFrameDuration();
                    Animation animation = null;

                    if( textureNames.size == 1) {
                        animation = loadAnimation(textureNames.get(0), points, frameDuration);
                    }else if( textureNames.size == 2){
                        animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration);
                    }

                    _animations.put(animationType, animation);
                }
            }
        }
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta){
        _frameTime = (_frameTime + delta)%5; //Want to avoid overflow

        //Look into the appropriate variable when changing position
        switch (_currentDirection) {
            case DOWN:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            case LEFT:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            case UP:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            case RIGHT:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            default:
                break;
        }

        batch.begin();
        batch.draw(_currentFrame, _currentPosition.x, _currentPosition.y, 1, 1);
        batch.end();

        //Used to graphically debug boundingboxes
        /*
        Rectangle rect = entity.getCurrentBoundingBox();
        Camera camera = mapMgr.getCamera();
        _shapeRenderer.setProjectionMatrix(camera.combined);
        _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE, rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight() * Map.UNIT_SCALE);
        _shapeRenderer.end();
        */
    }

    @Override
    public void dispose(){
    }
}
