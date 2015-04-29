package com.packtpub.libgdx.bludbourne;


public class TownMap extends Map{
    private static String _mapPath = "maps/town.tmx";

    TownMap(){
        super(MapFactory.MapType.TOWN, _mapPath);
    }
}
