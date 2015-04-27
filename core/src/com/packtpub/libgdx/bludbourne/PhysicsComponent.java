package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class PhysicsComponent implements Component {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    private Vector2 _velocity;

    private Rectangle _boundingBox;
    private Vector2 _nextEntityPosition;
    private Vector2 _currentEntityPosition;
    private Json _json;
    private Entity.State _state;
    private Entity.Direction _currentDirection;

    public PhysicsComponent(){
        this._boundingBox = new Rectangle();
        this._nextEntityPosition = new Vector2(0,0);
        this._currentEntityPosition = new Vector2(0,0);
        this._velocity = new Vector2(2f,2f);
        _json = new Json();
    }

    @Override
    public void dispose(){

    }

    @Override
    public void receiveMessage(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(Component.MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                _currentEntityPosition = _json.fromJson(Vector2.class, string[1]);
                _nextEntityPosition.set(_currentEntityPosition.x, _currentEntityPosition.y);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                _state = _json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                _currentDirection = _json.fromJson(Entity.Direction.class, string[1]);
            }
        }
    }

    public void update(Entity entity, MapManager mapMgr, float delta) {
        //We want the hitbox to be at the feet for a better feel
        setBoundingBoxSize(entity, 0f, 0.5f);

        if (!isCollisionWithMapLayer(mapMgr, _boundingBox) &&
                _state == Entity.State.WALKING){
            setNextPositionToCurrent(entity);

            Camera camera = mapMgr.getCamera();
            camera.position.set(_currentEntityPosition.x, _currentEntityPosition.y, 0f);
            camera.update();
        }

        updatePortalLayerActivation(mapMgr, _boundingBox);

        calculateNextPosition(delta);
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
                if( boundingBox.overlaps(rectangle) ){
                    //Gdx.app.debug(TAG, "Map Collision!");
                    return true;
                }
            }
        }

        return false;
    }

    private boolean updatePortalLayerActivation(MapManager mapMgr, Rectangle boundingBox){
        MapLayer mapPortalLayer =  mapMgr.getPortalLayer();

        if( mapPortalLayer == null ){
            return false;
        }

        Rectangle rectangle = null;

        for( MapObject object: mapPortalLayer.getObjects()){
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();

                if (boundingBox.overlaps(rectangle) ){
                    String mapName = object.getName();
                    if( mapName == null ) {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(_currentEntityPosition);
                    mapMgr.loadMap(mapName);

                    _currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    _nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

    public void setNextPositionToCurrent(Entity entity){
        this._currentEntityPosition.x = _nextEntityPosition.x;
        this._currentEntityPosition.y = _nextEntityPosition.y;

        entity.sendMessage(MESSAGE.CURRENT_POSITION,_json.toJson(_currentEntityPosition) );
    }

    public void calculateNextPosition(float deltaTime){
        if( _currentDirection == null ) return;

        float testX = _currentEntityPosition.x;
        float testY = _currentEntityPosition.y;

        _velocity.scl(deltaTime);

        switch (_currentDirection) {
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

        _nextEntityPosition.x = testX;
        _nextEntityPosition.y = testY;

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
            minX = _nextEntityPosition.x / MapManager.UNIT_SCALE;
            minY = _nextEntityPosition.y / MapManager.UNIT_SCALE;
        }else{
            minX = _nextEntityPosition.x;
            minY = _nextEntityPosition.y;
        }

        _boundingBox.set(minX, minY, width, height);
        //Gdx.app.debug(TAG, "SETTING Bounding Box: (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }
}
