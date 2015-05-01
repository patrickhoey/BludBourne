package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TownMap extends Map{
    private static String _mapPath = "maps/town.tmx";
    private static String _townGuardWalking = "scripts/town_guard_walking.json";

    private static String _townBlacksmith = "scripts/town_blacksmith.json";
    private static String TOWNBLACKSMITH = "TOWN_BLACKSMITH";

    private static String _townMage = "scripts/town_mage.json";
    private static String TOWNMAGE = "TOWN_MAGE";

    private static String _townInnKeeper = "scripts/town_innkeeper.json";
    private static String TOWNINNKEEPER = "TOWN_INNKEEPER";

    private Array<Entity> _mapEntities;

    TownMap(){
        super(MapFactory.MapType.TOWN, _mapPath);
        _mapEntities = new Array<Entity>(4);

        for( Vector2 position: _npcStartPositions){
            _mapEntities.add(initEntity(position, _townGuardWalking));
        }

        //Special cases
        _mapEntities.add(initSpecialEntity(TOWNBLACKSMITH, _townBlacksmith));
        _mapEntities.add(initSpecialEntity(TOWNMAGE, _townMage));
        _mapEntities.add(initSpecialEntity(TOWNINNKEEPER, _townInnKeeper));
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( Entity entity : _mapEntities ){
            entity.update(mapMgr, batch, delta);
        }
    }

    private Entity initEntity(Vector2 position, String configFile){
        Entity entity = EntityFactory.getEntity(EntityFactory.EntityType.NPC);
        entity.loadConfig(configFile);
        entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, _json.toJson(entity.getEntityConfig()));
        entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, _json.toJson(position));
        entity.sendMessage(Component.MESSAGE.INIT_STATE, _json.toJson(entity.getEntityConfig().getState()));
        entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, _json.toJson(entity.getEntityConfig().getDirection()));
        return entity;
    }

    private Entity initSpecialEntity(String positionName, String configFile){
        Vector2 position = new Vector2(0,0);

        if( _specialNPCStartPositions.containsKey(positionName) ) {
             position = _specialNPCStartPositions.get(positionName);
        }
        return initEntity(position, configFile);
    }
}
