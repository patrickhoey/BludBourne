package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class TopWorldMap extends Map{
    private static String _mapPath = "maps/topworld.tmx";

    private Array<Entity> _mapEntities;

    TopWorldMap(){
        super(MapFactory.MapType.TOP_WORLD, _mapPath);
        _mapEntities = new Array<Entity>(1);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta){
        for( Entity entity : _mapEntities ){
            entity.update(mapMgr, batch, delta);
        }
    }
}
