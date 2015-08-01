package com.packtpub.libgdx.bludbourne.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;

import java.util.Hashtable;

public class MonsterFactory {
    public static enum MonsterEntityType{
        MONSTER001,MONSTER002,MONSTER003,MONSTER004,MONSTER005,
        MONSTER006,MONSTER007,MONSTER008,MONSTER009,MONSTER010,
        MONSTER011,MONSTER012,MONSTER013,MONSTER014,MONSTER015,
        MONSTER016,MONSTER017,MONSTER018,MONSTER019,MONSTER020,
        MONSTER021,MONSTER022,MONSTER023,MONSTER024,MONSTER025,
        MONSTER026,MONSTER027,MONSTER028,MONSTER029,MONSTER030,
        MONSTER031,MONSTER032,MONSTER033,MONSTER034,MONSTER035,
        MONSTER036,MONSTER037,MONSTER038,MONSTER039,MONSTER040,
        MONSTER041, MONSTER042,
        NONE
    }

    private static MonsterFactory _instance = null;
    private Hashtable<String, Entity> _entities;
    private Hashtable<String, Array<MonsterEntityType>> _monsterZones;

    private MonsterFactory(){
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
