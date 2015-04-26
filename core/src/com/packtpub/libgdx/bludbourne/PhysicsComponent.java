package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PhysicsComponent {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    private Vector2 _velocity;

    public Rectangle _boundingBox;
    public Vector2 _nextPlayerPosition;
    public Vector2 _currentPlayerPosition;

    public PhysicsComponent(){
        this._boundingBox = new Rectangle();
        this._nextPlayerPosition = new Vector2(0,0);
        this._currentPlayerPosition = new Vector2(0,0);
        this._velocity = new Vector2(2f,2f);
    }

    public void dispose(){

    }

    public void update(MapManager mapMgr, Entity entity, float delta) {
        //We want the hitbox to be at the feet for a better feel
        setBoundingBoxSize(entity, 0f, 0.5f);

        if (!isCollisionWithMapLayer(mapMgr, _boundingBox) &&
                entity._state == Entity.State.WALKING){
            setNextPositionToCurrent();
        }

        calculateNextPosition(entity._direction, delta);

        //Gdx.app.debug(TAG, "update:: Next Position: (" + _nextPlayerPosition.x + "," + _nextPlayerPosition.y + ")" + "DELTA: " + delta);
    }

    private boolean isCollisionWithMapLayer(MapManager mapMgr, Rectangle boundingBox){
        MapLayer mapCollisionLayer =  mapMgr.getCollisionLayer();

        if( mapCollisionLayer == null ){
            return false;
        }

        Rectangle rectangle = null;

        for( MapObject object: mapCollisionLayer.getObjects()){
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                //Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
                //Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
                if( boundingBox.overlaps(rectangle) ){
                    //Gdx.app.debug(TAG, "Map Collision!");
                    return true;
                }
            }
        }

        return false;
    }

    public void init(float startX, float startY){
        this._currentPlayerPosition.x = startX;
        this._currentPlayerPosition.y = startY;

        this._nextPlayerPosition.x = startX;
        this._nextPlayerPosition.y = startY;

        //Gdx.app.debug(TAG, "Calling INIT" );
    }

    public Vector2 getCurrentPosition(){
        return _currentPlayerPosition;
    }

    public void setCurrentPosition(float currentPositionX, float currentPositionY){
        this._currentPlayerPosition.x = currentPositionX;
        this._currentPlayerPosition.y = currentPositionY;
    }

    public void setNextPositionToCurrent(){
        setCurrentPosition(_nextPlayerPosition.x, _nextPlayerPosition.y);
        //Gdx.app.debug(TAG, "Setting nextPosition as Current: (" + _nextPlayerPosition.x + "," + _nextPlayerPosition.y + ")");
    }

    public void calculateNextPosition(Entity.Direction currentDirection, float deltaTime){
        float testX = _currentPlayerPosition.x;
        float testY = _currentPlayerPosition.y;

        //Gdx.app.debug(TAG, "calculateNextPosition:: Current Position: (" + _currentPlayerPosition.x + "," + _currentPlayerPosition.y + ")"  );
        //Gdx.app.debug(TAG, "calculateNextPosition:: Current Direction: " + _currentDirection  );

        _velocity.scl(deltaTime);

        switch (currentDirection) {
            case LEFT :
                testX -=  _velocity.x;
                break;
            case RIGHT :
                testX += _velocity.x;
                break;
            case UP :
                testY += _velocity.y;
                break;
            case DOWN :
                testY -= _velocity.y;
                break;
            default:
                break;
        }

        _nextPlayerPosition.x = testX;
        _nextPlayerPosition.y = testY;

        //velocity
        _velocity.scl(1 / deltaTime);
    }

    public void setBoundingBoxSize(Entity entity, float percentageWidthReduced, float percentageHeightReduced){
        //Update the current bounding box
        float width;
        float height;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if( widthReductionAmount > 0 && widthReductionAmount < 1){
            width = entity.FRAME_WIDTH * widthReductionAmount;
        }else{
            width = entity.FRAME_WIDTH;
        }

        if( heightReductionAmount > 0 && heightReductionAmount < 1){
            height = entity.FRAME_HEIGHT * heightReductionAmount;
        }else{
            height = entity.FRAME_HEIGHT;
        }

        if( width == 0 || height == 0){
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;
        if( MapManager.UNIT_SCALE > 0 ) {
            minX = _nextPlayerPosition.x / MapManager.UNIT_SCALE;
            minY = _nextPlayerPosition.y / MapManager.UNIT_SCALE;
        }else{
            minX = _nextPlayerPosition.x;
            minY = _nextPlayerPosition.y;
        }

        _boundingBox.set(minX, minY, width, height);
        //Gdx.app.debug(TAG, "SETTING Bounding Box: (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }
}
