package com.packtpub.libgdx.bludbourne.battle;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;

import java.util.Hashtable;

public class MonsterFactory {
    public static enum MonsterEntityType{
        MONSTER001,
        NONE
    }

    private static MonsterFactory _instance = null;
    private Hashtable<String, Entity> _entities;

    public MonsterFactory(){
        Array<EntityConfig> configs = Entity.getEntityConfigs("scripts/monsters.json");
        _entities =  Entity.initEntities(configs);
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

}
