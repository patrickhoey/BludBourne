package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public abstract class Map {
    private static final String TAG = Map.class.getSimpleName();

    public final static float UNIT_SCALE  = 1/16f;

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";

    //Starting locations
    protected final static String PLAYER_START = "PLAYER_START";

    protected Vector2 _playerStartPositionRect;
    protected Vector2 _closestPlayerStartPosition;
    protected Vector2 _convertedUnits;
    protected TiledMap _currentMap = null;
    protected Vector2 _playerStart;

    protected MapLayer _collisionLayer = null;
    protected MapLayer _portalLayer = null;
    protected MapLayer _spawnsLayer = null;

    protected MapFactory.MapType _currentMapType;

    Map( MapFactory.MapType mapType, String fullMapPath){
        _currentMapType = mapType;
        _playerStart = new Vector2(0,0);
        _playerStartPositionRect = new Vector2(0,0);
        _closestPlayerStartPosition = new Vector2(0,0);
        _convertedUnits = new Vector2(0,0);

        if( fullMapPath == null || fullMapPath.isEmpty() ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        Utility.loadMapAsset(fullMapPath);
        if( Utility.isAssetLoaded(fullMapPath) ) {
            _currentMap = Utility.getMapAsset(fullMapPath);
        }else{
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        _collisionLayer = _currentMap.getLayers().get(COLLISION_LAYER);
        if( _collisionLayer == null ){
            Gdx.app.debug(TAG, "No collision layer!");
        }

        _portalLayer = _currentMap.getLayers().get(PORTAL_LAYER);
        if( _portalLayer == null ){
            Gdx.app.debug(TAG, "No portal layer!");
        }

        _spawnsLayer = _currentMap.getLayers().get(SPAWNS_LAYER);
        if( _spawnsLayer == null ){
            Gdx.app.debug(TAG, "No spawn layer!");
        }else{
            setClosestStartPosition(_playerStart);
        }
    }

    public Vector2 getPlayerStart() {
        return _playerStart;
    }

    public MapLayer getCollisionLayer(){
        return _collisionLayer;
    }

    public MapLayer getPortalLayer(){
        return _portalLayer;
    }

    public TiledMap getCurrentTiledMap() {
        return _currentMap;
    }

    public Vector2 getPlayerStartUnitScaled(){
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x * UNIT_SCALE, _playerStart.y * UNIT_SCALE);
        return playerStart;
    }

    private void setClosestStartPosition(final Vector2 position){
         Gdx.app.debug(TAG, "setClosestStartPosition INPUT: (" + position.x + "," + position.y + ") " + _currentMapType.toString());

        //Get last known position on this map
        _playerStartPositionRect.set(0,0);
        _closestPlayerStartPosition.set(0,0);
        float shortestDistance = 0f;

        //Go through all player start positions and choose closest to last known position
        for( MapObject object: _spawnsLayer.getObjects()){
            if( object.getName().equalsIgnoreCase(PLAYER_START) ){
                ((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
                float distance = position.dst2(_playerStartPositionRect);

                Gdx.app.debug(TAG, "DISTANCE: " + distance + " for " + _currentMapType.toString());

                if( distance < shortestDistance || shortestDistance == 0 ){
                    _closestPlayerStartPosition.set(_playerStartPositionRect);
                    shortestDistance = distance;
                    Gdx.app.debug(TAG, "closest START is: (" + _closestPlayerStartPosition.x + "," + _closestPlayerStartPosition.y + ") " +  _currentMapType.toString());
                }
            }
        }
        _playerStart =  _closestPlayerStartPosition.cpy();
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position){
        if( UNIT_SCALE <= 0 )
            return;

        _convertedUnits.set(position.x/UNIT_SCALE, position.y/UNIT_SCALE);
        setClosestStartPosition(_convertedUnits);
    }
}
