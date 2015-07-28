package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TownMap extends Map{
    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();

    private static String _mapPath = "maps/town.tmx";
    private static String _townGuardWalking = "scripts/town_guard_walking.json";
    private static String _townBlacksmith = "scripts/town_blacksmith.json";
    private static String _townMage = "scripts/town_mage.json";
    private static String _townInnKeeper = "scripts/town_innkeeper.json";
    private static String _townFolk = "scripts/town_folk.json";

    TownMap(){
        super(MapFactory.MapType.TOWN, _mapPath);

        for( Vector2 position: _npcStartPositions){
            _mapEntities.add(Entity.initEntity(Entity.loadEntityConfigByPath(_townGuardWalking), position));
        }

        //Special cases
        _mapEntities.add(initSpecialEntity(Entity.loadEntityConfigByPath(_townBlacksmith)));
        _mapEntities.add(initSpecialEntity(Entity.loadEntityConfigByPath(_townMage)));
        _mapEntities.add(initSpecialEntity(Entity.loadEntityConfigByPath(_townInnKeeper)));

        //When we have multiple configs in one file
        Array<EntityConfig> configs = Entity.getEntityConfigs(_townFolk);
        for(EntityConfig config: configs){
            _mapEntities.add(initSpecialEntity(Entity.loadEntityConfig(config)));
        }
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( int i=0; i < _mapEntities.size; i++){
            _mapEntities.get(i).update(mapMgr, batch, delta);
        }
        for( int i=0; i < _mapQuestEntities.size; i++){
            _mapQuestEntities.get(i).update(mapMgr, batch, delta);
        }
    }

    private Entity initSpecialEntity(EntityConfig entityConfig){
        Vector2 position = new Vector2(0,0);

        if( _specialNPCStartPositions.containsKey(entityConfig.getEntityID()) ) {
             position = _specialNPCStartPositions.get(entityConfig.getEntityID());
        }
        return Entity.initEntity(entityConfig, position);
    }
}
