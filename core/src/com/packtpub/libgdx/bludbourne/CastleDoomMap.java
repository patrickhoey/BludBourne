package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;
import com.packtpub.libgdx.bludbourne.sfx.ParticleEffectFactory;

public class CastleDoomMap extends Map{
    private static String _mapPath = "maps/castle_of_doom.tmx";

    CastleDoomMap(){
        super(MapFactory.MapType.CASTLE_OF_DOOM, _mapPath);

        Array<Vector2> candleEffectPositions = getParticleEffectSpawnPositions(ParticleEffectFactory.ParticleEffectType.CANDLE_FIRE);
        for( Vector2 position: candleEffectPositions ){
            _mapParticleEffects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.CANDLE_FIRE, position));
        }

        Array<Vector2> lavaSmokeEffectPositions = getParticleEffectSpawnPositions(ParticleEffectFactory.ParticleEffectType.LAVA_SMOKE);
        for( Vector2 position: lavaSmokeEffectPositions ){
            _mapParticleEffects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.LAVA_SMOKE, position));
        }
    }

    @Override
    public void unloadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_CASTLEDOOM);
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_CASTLEDOOM);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_CASTLEDOOM);
    }

}
