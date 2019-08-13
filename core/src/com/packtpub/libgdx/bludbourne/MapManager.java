package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileObserver;
import com.packtpub.libgdx.bludbourne.sfx.ClockActor;

public class MapManager implements ProfileObserver {
    private static final String TAG = MapManager.class.getSimpleName();

    private Camera _camera;
    private boolean _mapChanged = false;
    private Map _currentMap;
    private Entity _player;
    private Entity _currentSelectedEntity = null;
    private MapLayer _currentLightMap = null;
    private MapLayer _previousLightMap = null;
    private ClockActor.TimeOfDay _timeOfDay = null;
    private float _currentLightMapOpacity = 0;
    private float _previousLightMapOpacity = 1;
    private boolean _timeOfDayChanged = false;

    public MapManager(){
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch(event){
            case PROFILE_LOADED:
                String currentMap = profileManager.getProperty("currentMapType", String.class);
                MapFactory.MapType mapType;
                if( currentMap == null || currentMap.isEmpty() ){
                    mapType = MapFactory.MapType.TOWN;
                }else{
                    mapType = MapFactory.MapType.valueOf(currentMap);
                }
                loadMap(mapType);

                Vector2 topWorldMapStartPosition = profileManager.getProperty("topWorldMapStartPosition", Vector2.class);
                if( topWorldMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.TOP_WORLD).setPlayerStart(topWorldMapStartPosition);
                }

                Vector2 castleOfDoomMapStartPosition = profileManager.getProperty("castleOfDoomMapStartPosition", Vector2.class);
                if( castleOfDoomMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).setPlayerStart(castleOfDoomMapStartPosition);
                }

                Vector2 townMapStartPosition = profileManager.getProperty("townMapStartPosition", Vector2.class);
                if( townMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.TOWN).setPlayerStart(townMapStartPosition);
                }

                break;
            case SAVING_PROFILE:
                if( _currentMap != null ){
                    profileManager.setProperty("currentMapType", _currentMap._currentMapType.toString());
                }

                profileManager.setProperty("topWorldMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart() );
                profileManager.setProperty("castleOfDoomMapStartPosition", MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart() );
                profileManager.setProperty("townMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOWN).getPlayerStart() );
                break;
            case CLEAR_CURRENT_PROFILE:
                _currentMap = null;
                profileManager.setProperty("currentMapType", MapFactory.MapType.TOWN.toString());

                MapFactory.clearCache();

                profileManager.setProperty("topWorldMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart() );
                profileManager.setProperty("castleOfDoomMapStartPosition", MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart() );
                profileManager.setProperty("townMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOWN).getPlayerStart() );
                break;
            default:
                break;
        }
    }

    public void loadMap(MapFactory.MapType mapType){
        Map map = MapFactory.getMap(mapType);

        if( map == null ){
            Gdx.app.debug(TAG, "Map does not exist!  ");
            return;
        }

        if( _currentMap != null ){
            _currentMap.unloadMusic();
            if( _previousLightMap != null ){
                _previousLightMap.setOpacity(0);
                _previousLightMap = null;
            }
            if( _currentLightMap != null ){
                _currentLightMap.setOpacity(1);
                _currentLightMap = null;
            }
        }

        map.loadMusic();

        _currentMap = map;
        _mapChanged = true;
        clearCurrentSelectedMapEntity();
        Gdx.app.debug(TAG, "Player Start: (" + _currentMap.getPlayerStart().x + "," + _currentMap.getPlayerStart().y + ")");
    }

    public void unregisterCurrentMapEntityObservers(){
        if( _currentMap != null ){
            Array<Entity> entities = _currentMap.getMapEntities();
            for(Entity entity: entities){
                entity.unregisterObservers();
            }

            Array<Entity> questEntities = _currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities){
                questEntity.unregisterObservers();
            }
        }
    }

    public void registerCurrentMapEntityObservers(ComponentObserver observer){
        if( _currentMap != null ){
            Array<Entity> entities = _currentMap.getMapEntities();
            for(Entity entity: entities){
                entity.registerObserver(observer);
            }

            Array<Entity> questEntities = _currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities){
                questEntity.registerObserver(observer);
            }
        }
    }


    public void disableCurrentmapMusic(){
        _currentMap.unloadMusic();
    }

    public void enableCurrentmapMusic(){
        _currentMap.loadMusic();
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        _currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer(){
        return _currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer(){
        return _currentMap.getPortalLayer();
    }

    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID) {
        return _currentMap.getQuestItemSpawnPositions(objectName, objectTaskID);
    }

    public MapLayer getQuestDiscoverLayer(){
        return _currentMap.getQuestDiscoverLayer();
    }

    public MapLayer getEnemySpawnLayer(){
        return _currentMap.getEnemySpawnLayer();
    }

    public MapFactory.MapType getCurrentMapType(){
        return _currentMap.getCurrentMapType();
    }

    public Vector2 getPlayerStartUnitScaled() {
        return _currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap(){
        if( _currentMap == null ) {
            loadMap(MapFactory.MapType.TOWN);
        }
        return _currentMap.getCurrentTiledMap();
    }

    public MapLayer getPreviousLightMapLayer(){
        return _previousLightMap;
    }

    public MapLayer getCurrentLightMapLayer(){
        return _currentLightMap;
    }

    public void updateLightMaps(ClockActor.TimeOfDay timeOfDay){
        if( _timeOfDay != timeOfDay ){
            _currentLightMapOpacity = 0;
            _previousLightMapOpacity = 1;
            _timeOfDay = timeOfDay;
            _timeOfDayChanged = true;
            _previousLightMap = _currentLightMap;

            Gdx.app.debug(TAG, "Time of Day CHANGED");
        }
        switch(timeOfDay){
            case DAWN:
                _currentLightMap = _currentMap.getLightMapDawnLayer();
                break;
            case AFTERNOON:
                _currentLightMap = _currentMap.getLightMapAfternoonLayer();
                break;
            case DUSK:
                _currentLightMap = _currentMap.getLightMapDuskLayer();
                break;
            case NIGHT:
                _currentLightMap = _currentMap.getLightMapNightLayer();
                break;
            default:
                _currentLightMap = _currentMap.getLightMapAfternoonLayer();
                break;
        }

            if( _timeOfDayChanged ){
                if( _previousLightMap != null && _previousLightMapOpacity != 0 ){
                    _previousLightMap.setOpacity(_previousLightMapOpacity);
                    _previousLightMapOpacity = MathUtils.clamp(_previousLightMapOpacity -= .05, 0, 1);

                    if( _previousLightMapOpacity == 0 ){
                        _previousLightMap = null;
                    }
                }

                if( _currentLightMap != null && _currentLightMapOpacity != 1 ) {
                    _currentLightMap.setOpacity(_currentLightMapOpacity);
                    _currentLightMapOpacity = MathUtils.clamp(_currentLightMapOpacity += .01, 0, 1);
                }
            }else{
                _timeOfDayChanged = false;
            }
    }

    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta){
        _currentMap.updateMapEntities(mapMgr, batch, delta);
    }

    public void updateCurrentMapEffects(MapManager mapMgr, Batch batch, float delta){
        _currentMap.updateMapEffects(mapMgr, batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities(){
        return _currentMap.getMapEntities();
    }

    public final Array<Entity> getCurrentMapQuestEntities(){
        return _currentMap.getMapQuestEntities();
    }

    public void addMapQuestEntities(Array<Entity> entities){
        _currentMap.getMapQuestEntities().addAll(entities);
    }

    public void removeMapQuestEntity(Entity entity){
        entity.unregisterObservers();

        Array<Vector2> positions = ProfileManager.getInstance().getProperty(entity.getEntityConfig().getEntityID(), Array.class);
        if( positions == null ) return;

        for( Vector2 position : positions){
            if( position.x == entity.getCurrentPosition().x &&
                    position.y == entity.getCurrentPosition().y ){
                positions.removeValue(position, true);
                break;
            }
        }
        _currentMap.getMapQuestEntities().removeValue(entity, true);
        ProfileManager.getInstance().setProperty(entity.getEntityConfig().getEntityID(), positions);
    }

    public void clearAllMapQuestEntities(){
        _currentMap.getMapQuestEntities().clear();
    }

    public Entity getCurrentSelectedMapEntity(){
        return _currentSelectedEntity;
    }

    public void setCurrentSelectedMapEntity(Entity currentSelectedEntity) {
        this._currentSelectedEntity = currentSelectedEntity;
    }

    public void clearCurrentSelectedMapEntity(){
        if( _currentSelectedEntity == null ) return;
        _currentSelectedEntity.sendMessage(Component.MESSAGE.ENTITY_DESELECTED);
        _currentSelectedEntity = null;
    }

    public void setPlayer(Entity entity){
        this._player = entity;
    }

    public Entity getPlayer(){
        return this._player;
    }

    public void setCamera(Camera camera){
        this._camera = camera;
    }

    public Camera getCamera(){
        return _camera;
    }

    public boolean hasMapChanged(){
        return _mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged){
        this._mapChanged = hasMapChanged;
    }
}
