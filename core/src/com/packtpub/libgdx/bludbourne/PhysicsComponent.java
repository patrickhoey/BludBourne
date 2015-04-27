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
    private Vector2 _nextPlayerPosition;
    private Vector2 _currentPlayerPosition;
    private Json _json;

    public PhysicsComponent(){
        this._boundingBox = new Rectangle();
        this._nextPlayerPosition = new Vector2(0,0);
        this._currentPlayerPosition = new Vector2(0,0);
        this._velocity = new Vector2(2f,2f);
        _json = new Json();
    }

    @Override
    public void dispose(){

    }

    @Override
    public void receive(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(MESSAGE.MESSAGE_TOKEN);

        if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION)) {
            _currentPlayerPosition = _json.fromJson(Vector2.class, string[1]);
            _nextPlayerPosition.set(_currentPlayerPosition.x, _currentPlayerPosition.y);
        }
    }

    public void update(Entity entity, MapManager mapMgr, float delta) {
        //We want the hitbox to be at the feet for a better feel
        setBoundingBoxSize(entity, 0f, 0.5f);

        if (!isCollisionWithMapLayer(mapMgr, _boundingBox) &&
                entity._state == Entity.State.WALKING){
            setNextPositionToCurrent(entity);

            //Preferable to lock and center the _camera to the player's position
            Camera camera = mapMgr.getCamera();
            camera.position.set(_currentPlayerPosition.x, _currentPlayerPosition.y, 0f);
            camera.update();
        }

        updatePortalLayerActivation(mapMgr, _boundingBox);

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

    private boolean updatePortalLayerActivation(MapManager mapMgr, Rectangle boundingBox){
        MapLayer mapPortalLayer =  mapMgr.getPortalLayer();

        if( mapPortalLayer == null ){
            return false;
        }

        Rectangle rectangle = null;

        for( MapObject object: mapPortalLayer.getObjects()){
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                //Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
                //Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
                if (boundingBox.overlaps(rectangle) ){
                    String mapName = object.getName();
                    if( mapName == null ) {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(_currentPlayerPosition);
                    mapMgr.loadMap(mapName);

                    _currentPlayerPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _currentPlayerPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    _nextPlayerPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _nextPlayerPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

    public void setNextPositionToCurrent(Entity entity){
        this._currentPlayerPosition.x = _nextPlayerPosition.x;
        this._currentPlayerPosition.y = _nextPlayerPosition.y;

        String text = MESSAGE.CURRENT_POSITION + MESSAGE.MESSAGE_TOKEN +_json.toJson(_currentPlayerPosition);
        entity.send(text);
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
