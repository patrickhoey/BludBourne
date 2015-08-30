package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectFactory {

    public static enum ParticleEffectType{
        CANDLE_FIRE, LAVA_SMOKE, WAND_ATTACK,
        NONE
    }

    private static ParticleEffectFactory _instance = null;

    private ParticleEffectFactory(){
    }

    public static ParticleEffectFactory getInstance() {
        if (_instance == null) {
            _instance = new ParticleEffectFactory();
        }

        return _instance;
    }

    public static ParticleEffect getParticleEffect(ParticleEffectType particleEffectType){
        ParticleEffect effect = new ParticleEffect();
        switch(particleEffectType){
            case CANDLE_FIRE:
                effect.load(Gdx.files.internal("sfx/candle.p"), Gdx.files.internal("sfx"));
                return effect;
            case LAVA_SMOKE:
                return null;
            case WAND_ATTACK:
                return null;
            default:
                return null;
        }

    }

}
