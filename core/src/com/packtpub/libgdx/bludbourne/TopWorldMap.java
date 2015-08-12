package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;

public class TopWorldMap extends Map{
    private static String _mapPath = "maps/topworld.tmx";

    TopWorldMap(){
        super(MapFactory.MapType.TOP_WORLD, _mapPath);
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

    @Override
    public void unloadMusic() {

    }

    @Override
    public void loadMusic() {

    }
}
