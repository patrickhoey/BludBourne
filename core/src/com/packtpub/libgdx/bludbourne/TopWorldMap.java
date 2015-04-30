package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.utils.Array;

public class TopWorldMap extends Map{
    private static String _mapPath = "maps/topworld.tmx";

    Array<Entity> mapEntities;

    TopWorldMap(){
        super(MapFactory.MapType.TOP_WORLD, _mapPath);

        mapEntities = new Array<Entity>(1);

        mapEntities.add(EntityFactory.getEntity(EntityFactory.EntityType.NPC));


    }
}
