package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

public class ParticleEffectFactory {

    private static String SFX_ROOT_DIR = "sfx";

    public static enum ParticleEffectType{
        CANDLE_FIRE("sfx/candle.p"),
        LANTERN_FIRE("sfx/candle.p"),
        LAVA_SMOKE("sfx/smoke.p"),
        WAND_ATTACK("sfx/magic_attack.p"),
        NONE("");

        private String _fullFilePath;

        ParticleEffectType(String fullFilePath){
            this._fullFilePath = fullFilePath;
        }

        public String getValue(){
            return _fullFilePath;
        }
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

    public static ParticleEffect getParticleEffect(ParticleEffectType particleEffectType, float positionX, float positionY){
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particleEffectType.getValue()), Gdx.files.internal(SFX_ROOT_DIR));
        effect.setPosition(positionX, positionY);
        switch(particleEffectType){
            case CANDLE_FIRE:
                effect.scaleEffect(.04f);
                break;
            case LANTERN_FIRE:
                effect.scaleEffect(.02f);
                break;
            case LAVA_SMOKE:
                effect.scaleEffect(.04f);
                break;
            case WAND_ATTACK:
                effect.scaleEffect(1.0f);
                break;
            default:
                break;
        }
        effect.start();
        return effect;
    }

    public static ParticleEffect getParticleEffect(ParticleEffectType particleEffectType, Vector2 position){
        return getParticleEffect(particleEffectType, position.x, position.y);
    }

}
