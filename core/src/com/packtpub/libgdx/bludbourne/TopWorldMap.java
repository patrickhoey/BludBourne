package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;
import com.packtpub.libgdx.bludbourne.sfx.ParticleEffectFactory;

public class TopWorldMap extends Map{
    private static String _mapPath = "maps/topworld.tmx";

    TopWorldMap(){
        super(MapFactory.MapType.TOP_WORLD, _mapPath);

        Array<Vector2> lanternEffectPositions = getParticleEffectSpawnPositions(ParticleEffectFactory.ParticleEffectType.LANTERN_FIRE);
        for( Vector2 position: lanternEffectPositions ){
            _mapParticleEffects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.LANTERN_FIRE, position));
        }
        Array<Vector2> lavaSmokeEffectPositions = getParticleEffectSpawnPositions(ParticleEffectFactory.ParticleEffectType.LAVA_SMOKE);
        for( Vector2 position: lavaSmokeEffectPositions ){
            _mapParticleEffects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.LAVA_SMOKE, position));
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
