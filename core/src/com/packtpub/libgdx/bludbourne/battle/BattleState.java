package com.packtpub.libgdx.bludbourne.battle;

import com.packtpub.libgdx.bludbourne.Entity;

public class BattleState extends BattleSubject {
    private Entity _currentOpponent;

    private void setCurrentOpponent(MonsterFactory.MonsterEntityType monsterType){
        Entity entity = MonsterFactory.getInstance().getMonster(monsterType);
        if( entity == null ) return;
        this._currentOpponent = entity;
        notify(entity, BattleObserver.BattleEvent.OPPONENT_ADDED);
    }

    public void battleZoneEntered(int battleZoneID){
        switch(battleZoneID){
            case 1:
                setCurrentOpponent(MonsterFactory.MonsterEntityType.MONSTER001);
                break;
            default:
                break;
        }
    }
}
