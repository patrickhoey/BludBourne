package com.packtpub.libgdx.bludbourne.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;

import java.util.Hashtable;

public class MonsterFactory {
    public static enum MonsterEntityType{
        MONSTER001,
        MONSTER002,
        MONSTER003,
        MONSTER004,
        MONSTER005,
        NONE
    }

    private static MonsterFactory _instance = null;
    private Hashtable<String, Entity> _entities;
    private Hashtable<String, Array<MonsterEntityType>> _monsterZones;

    public MonsterFactory(){
        Array<EntityConfig> configs = Entity.getEntityConfigs("scripts/monsters.json");
        _entities =  Entity.initEntities(configs);

        _monsterZones = MonsterZone.getMonsterZones("scripts/monster_zones.json");
    }

    public static MonsterFactory getInstance() {
        if (_instance == null) {
            _instance = new MonsterFactory();
        }

        return _instance;
    }

    public Entity getMonster(MonsterEntityType monsterEntityType){
        Entity entity = _entities.get(monsterEntityType.toString());
        return new Entity(entity);
    }

    public Entity getRandomMonster(int monsterZoneID){
        Array<MonsterEntityType> monsters = _monsterZones.get(String.valueOf(monsterZoneID));
        int size = monsters.size;
        if( size == 0 ){
            return null;
        }
        int randomIndex = MathUtils.random(size - 1);

        return getMonster(monsters.get(randomIndex));
    }

}
