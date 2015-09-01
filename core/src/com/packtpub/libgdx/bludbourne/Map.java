package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.audio.AudioManager;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;
import com.packtpub.libgdx.bludbourne.audio.AudioSubject;
import com.packtpub.libgdx.bludbourne.sfx.ParticleEffectFactory;

import java.util.Hashtable;

public abstract class Map implements AudioSubject{
    private static final String TAG = Map.class.getSimpleName();

    public final static float UNIT_SCALE  = 1/16f;

    private Array<AudioObserver> _observers;

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";
    protected final static String QUEST_ITEM_SPAWN_LAYER = "MAP_QUEST_ITEM_SPAWN_LAYER";
    protected final static String QUEST_DISCOVER_LAYER = "MAP_QUEST_DISCOVER_LAYER";
    protected final static String ENEMY_SPAWN_LAYER = "MAP_ENEMY_SPAWN_LAYER";
    protected final static String PARTICLE_EFFECT_SPAWN_LAYER = "PARTICLE_EFFECT_SPAWN_LAYER";

    public final static String BACKGROUND_LAYER = "Background_Layer";
    public final static String GROUND_LAYER = "Ground_Layer";
    public final static String DECORATION_LAYER = "Decoration_Layer";

    public final static String LIGHTMAP_DAWN_LAYER = "MAP_LIGHTMAP_LAYER_DAWN";
    public final static String LIGHTMAP_AFTERNOON_LAYER = "MAP_LIGHTMAP_LAYER_AFTERNOON";
    public final static String LIGHTMAP_DUSK_LAYER = "MAP_LIGHTMAP_LAYER_DUSK";
    public final static String LIGHTMAP_NIGHT_LAYER = "MAP_LIGHTMAP_LAYER_NIGHT";

    //Starting locations
    protected final static String PLAYER_START = "PLAYER_START";
    protected final static String NPC_START = "NPC_START";

    protected Json _json;

    protected Vector2 _playerStartPositionRect;
    protected Vector2 _closestPlayerStartPosition;
    protected Vector2 _convertedUnits;
    protected TiledMap _currentMap = null;
    protected Vector2 _playerStart;
    protected Array<Vector2> _npcStartPositions;
    protected Hashtable<String, Vector2> _specialNPCStartPositions;

    protected MapLayer _collisionLayer = null;
    protected MapLayer _portalLayer = null;
    protected MapLayer _spawnsLayer = null;
    protected MapLayer _questItemSpawnLayer = null;
    protected MapLayer _questDiscoverLayer = null;
    protected MapLayer _enemySpawnLayer = null;
    protected MapLayer _particleEffectSpawnLayer = null;

    protected MapLayer _lightMapDawnLayer = null;
    protected MapLayer _lightMapAfternoonLayer = null;
    protected MapLayer _lightMapDuskLayer = null;
    protected MapLayer _lightMapNightLayer = null;

    protected MapFactory.MapType _currentMapType;
    protected Array<Entity> _mapEntities;
    protected Array<Entity> _mapQuestEntities;
    protected Array<ParticleEffect> _mapParticleEffects;

    Map( MapFactory.MapType mapType, String fullMapPath){
        _json = new Json();
        _mapEntities = new Array<Entity>(10);
        _observers = new Array<AudioObserver>();
        _mapQuestEntities = new Array<Entity>();
        _mapParticleEffects = new Array<ParticleEffect>();
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

        _questItemSpawnLayer = _currentMap.getLayers().get(QUEST_ITEM_SPAWN_LAYER);
        if( _questItemSpawnLayer == null ){
            Gdx.app.debug(TAG, "No quest item spawn layer!");
        }

        _questDiscoverLayer = _currentMap.getLayers().get(QUEST_DISCOVER_LAYER);
        if( _questDiscoverLayer == null ){
            Gdx.app.debug(TAG, "No quest discover layer!");
        }

        _enemySpawnLayer = _currentMap.getLayers().get(ENEMY_SPAWN_LAYER);
        if( _enemySpawnLayer == null ){
            Gdx.app.debug(TAG, "No enemy layer found!");
        }

        _lightMapDawnLayer = _currentMap.getLayers().get(LIGHTMAP_DAWN_LAYER);
        if( _lightMapDawnLayer == null ){
            Gdx.app.debug(TAG, "No dawn lightmap layer found!");
        }

        _lightMapAfternoonLayer = _currentMap.getLayers().get(LIGHTMAP_AFTERNOON_LAYER);
        if( _lightMapAfternoonLayer == null ){
            Gdx.app.debug(TAG, "No afternoon lightmap layer found!");
        }


        _lightMapDuskLayer = _currentMap.getLayers().get(LIGHTMAP_DUSK_LAYER);
        if( _lightMapDuskLayer == null ){
            Gdx.app.debug(TAG, "No dusk lightmap layer found!");
        }

        _lightMapNightLayer = _currentMap.getLayers().get(LIGHTMAP_NIGHT_LAYER);
        if( _lightMapNightLayer == null ){
            Gdx.app.debug(TAG, "No night lightmap layer found!");
        }

        _particleEffectSpawnLayer = _currentMap.getLayers().get(PARTICLE_EFFECT_SPAWN_LAYER);
        if( _particleEffectSpawnLayer == null ){
            Gdx.app.debug(TAG, "No particle effect spawn layer!");
        }

        _npcStartPositions = getNPCStartPositions();
        _specialNPCStartPositions = getSpecialNPCStartPositions();

        //Observers
        this.addObserver(AudioManager.getInstance());
    }

    public MapLayer getLightMapDawnLayer(){
        return _lightMapDawnLayer;
    }

    public MapLayer getLightMapAfternoonLayer(){
        return _lightMapAfternoonLayer;
    }

    public MapLayer getLightMapDuskLayer(){
        return _lightMapDuskLayer;
    }

    public MapLayer getLightMapNightLayer(){
        return _lightMapNightLayer;
    }

    public Array<Vector2> getParticleEffectSpawnPositions(ParticleEffectFactory.ParticleEffectType particleEffectType) {
        Array<MapObject> objects = new Array<MapObject>();
        Array<Vector2> positions = new Array<Vector2>();

        for( MapObject object: _particleEffectSpawnLayer.getObjects()){
            String name = object.getName();

            if(     name == null || name.isEmpty() ||
                    !name.equalsIgnoreCase(particleEffectType.toString())){
                continue;
            }

            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            //Get center of rectangle
            float x = rect.getX() + (rect.getWidth()/2);
            float y = rect.getY() + (rect.getHeight()/2);

            //scale by the unit to convert from map coordinates
            x *= UNIT_SCALE;
            y *= UNIT_SCALE;

            positions.add(new Vector2(x,y));
        }
        return positions;
    }

    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID) {
        Array<MapObject> objects = new Array<MapObject>();
        Array<Vector2> positions = new Array<Vector2>();

        for( MapObject object: _questItemSpawnLayer.getObjects()){
            String name = object.getName();
            String taskID = (String)object.getProperties().get("taskID");

            if(        name == null || taskID == null ||
                       name.isEmpty() || taskID.isEmpty() ||
                       !name.equalsIgnoreCase(objectName) ||
                       !taskID.equalsIgnoreCase(objectTaskID)){
                continue;
            }
            //Get center of rectangle
            float x = ((RectangleMapObject)object).getRectangle().getX();
            float y = ((RectangleMapObject)object).getRectangle().getY();

            //scale by the unit to convert from map coordinates
            x *= UNIT_SCALE;
            y *= UNIT_SCALE;

            positions.add(new Vector2(x,y));
        }
        return positions;
    }

    public Array<Entity> getMapEntities(){
        return _mapEntities;
    }

    public Array<Entity> getMapQuestEntities(){
        return _mapQuestEntities;
    }

    public Array<ParticleEffect> getMapParticleEffects(){
        return _mapParticleEffects;
    }

    public void addMapQuestEntities(Array<Entity> entities){
        _mapQuestEntities.addAll(entities);
    }

    public MapFactory.MapType getCurrentMapType(){
        return _currentMapType;
    }

    public Vector2 getPlayerStart() {
        return _playerStart;
    }

    public void setPlayerStart(Vector2 playerStart) {
        this._playerStart = playerStart;
    }

    protected void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( int i=0; i < _mapEntities.size; i++){
            _mapEntities.get(i).update(mapMgr, batch, delta);
        }
        for( int i=0; i < _mapQuestEntities.size; i++){
            _mapQuestEntities.get(i).update(mapMgr, batch, delta);
        }
    }

    protected void updateMapEffects(MapManager mapMgr, Batch batch, float delta){
        for( int i=0; i < _mapParticleEffects.size; i++){
            batch.begin();
            _mapParticleEffects.get(i).draw(batch, delta);
            batch.end();
        }
    }

    protected void dispose(){
        for( int i=0; i < _mapEntities.size; i++){
            _mapEntities.get(i).dispose();
        }
        for( int i=0; i < _mapQuestEntities.size; i++){
            _mapQuestEntities.get(i).dispose();
        }
        for( int i=0; i < _mapParticleEffects.size; i++){
            _mapParticleEffects.get(i).dispose();
        }
    }

    public MapLayer getCollisionLayer(){
        return _collisionLayer;
    }

    public MapLayer getPortalLayer(){
        return _portalLayer;
    }

    public MapLayer getQuestItemSpawnLayer(){
        return _questItemSpawnLayer;
    }

    public MapLayer getQuestDiscoverLayer(){
        return _questDiscoverLayer;
    }

    public MapLayer getEnemySpawnLayer() {
        return _enemySpawnLayer;
    }

    public TiledMap getCurrentTiledMap() {
        return _currentMap;
    }

    public Vector2 getPlayerStartUnitScaled(){
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x * UNIT_SCALE, _playerStart.y * UNIT_SCALE);
        return playerStart;
    }

    private Array<Vector2> getNPCStartPositions(){
        Array<Vector2> npcStartPositions = new Array<Vector2>();

        for( MapObject object: _spawnsLayer.getObjects()){
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            if( objectName.equalsIgnoreCase(NPC_START) ){
                //Get center of rectangle
                float x = ((RectangleMapObject)object).getRectangle().getX();
                float y = ((RectangleMapObject)object).getRectangle().getY();

                //scale by the unit to convert from map coordinates
                x *= UNIT_SCALE;
                y *= UNIT_SCALE;

                npcStartPositions.add(new Vector2(x,y));
            }
        }
        return npcStartPositions;
    }

    private Hashtable<String, Vector2> getSpecialNPCStartPositions(){
        Hashtable<String, Vector2> specialNPCStartPositions = new Hashtable<String, Vector2>();

        for( MapObject object: _spawnsLayer.getObjects()){
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            //This is meant for all the special spawn locations, a catch all, so ignore known ones
            if(     objectName.equalsIgnoreCase(NPC_START) ||
                    objectName.equalsIgnoreCase(PLAYER_START) ){
                continue;
            }

            //Get center of rectangle
            float x = ((RectangleMapObject)object).getRectangle().getX();
            float y = ((RectangleMapObject)object).getRectangle().getY();

            //scale by the unit to convert from map coordinates
            x *= UNIT_SCALE;
            y *= UNIT_SCALE;

            specialNPCStartPositions.put(objectName, new Vector2(x,y));
        }
        return specialNPCStartPositions;
    }

    private void setClosestStartPosition(final Vector2 position){
         Gdx.app.debug(TAG, "setClosestStartPosition INPUT: (" + position.x + "," + position.y + ") " + _currentMapType.toString());

        //Get last known position on this map
        _playerStartPositionRect.set(0,0);
        _closestPlayerStartPosition.set(0,0);
        float shortestDistance = 0f;

        //Go through all player start positions and choose closest to last known position
        for( MapObject object: _spawnsLayer.getObjects()){
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            if( objectName.equalsIgnoreCase(PLAYER_START) ){
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

    abstract public void unloadMusic();
    abstract public void loadMusic();

    @Override
    public void addObserver(AudioObserver audioObserver) {
        _observers.add(audioObserver);
    }

    @Override
    public void removeObserver(AudioObserver audioObserver) {
        _observers.removeValue(audioObserver, true);
    }

    @Override
    public void removeAllObservers() {
        _observers.removeAll(_observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer: _observers){
            observer.onNotify(command, event);
        }
    }
}
