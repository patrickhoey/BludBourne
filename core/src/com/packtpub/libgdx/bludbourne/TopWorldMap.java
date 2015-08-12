package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;

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
        notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TOPWORLD);
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_TOPWORLD);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_TOPWORLD);
    }
}
