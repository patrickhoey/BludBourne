package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TownMap extends Map{
    private static String _mapPath = "maps/town.tmx";
    private static String _townGuardWalking = "scripts/town_guard_walking.json";

    private Array<Entity> _mapEntities;

    TownMap(){
        super(MapFactory.MapType.TOWN, _mapPath);
        _mapEntities = new Array<Entity>(4);

        for( Vector2 position: _npcStartPositions){
            Entity entity = EntityFactory.getEntity(EntityFactory.EntityType.NPC);
            entity.loadConfig(_townGuardWalking);
            entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, _json.toJson(entity.getEntityConfig()));
            entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, _json.toJson(position));
            _mapEntities.add(entity);
        }
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( Entity entity : _mapEntities ){
            entity.update(mapMgr, batch, delta);
        }
    }
}
