package com.packtpub.libgdx.bludbourne.battle;

import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;
import com.packtpub.libgdx.bludbourne.UI.InventoryObserver;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

public class BattleState extends BattleSubject implements InventoryObserver {
    private Entity _currentOpponent;
    private int _currentPlayerAP;
    private int _currentPlayerDP;

    public void setCurrentOpponent(int battleZoneLevel){
        System.out.print("Entered BATTLE ZONE: " + battleZoneLevel);
        Entity entity = MonsterFactory.getInstance().getRandomMonster(battleZoneLevel);
        if( entity == null ) return;
        this._currentOpponent = entity;
        notify(entity, BattleObserver.BattleEvent.OPPONENT_ADDED);
    }

    public void playerAttacks(){
        if( _currentOpponent == null ){
            return;
        }
        notify(_currentOpponent, BattleObserver.BattleEvent.PLAYER_TURN_START);

        int currentOpponentHP = Integer.parseInt(_currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HEALTH_POINTS.toString()));
        int currentOpponentDP = Integer.parseInt(_currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_DEFENSE_POINTS.toString()));

        int damage = MathUtils.clamp( _currentPlayerAP - currentOpponentDP, 0, _currentPlayerAP);

        currentOpponentHP = MathUtils.clamp( currentOpponentHP - damage, 0, currentOpponentHP);
        _currentOpponent.getEntityConfig().setPropertyValue(EntityConfig.EntityProperties.ENTITY_HEALTH_POINTS.toString(), String.valueOf(currentOpponentHP));
        System.out.println("Player attacks " + _currentOpponent.getEntityConfig().getEntityID() + " leaving it with HP: " + currentOpponentHP);

        _currentOpponent.getEntityConfig().setPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString(), String.valueOf(damage));
        notify(_currentOpponent, BattleObserver.BattleEvent.OPPONENT_HIT_DAMAGE);

        if( currentOpponentHP == 0 ){
            notify(_currentOpponent, BattleObserver.BattleEvent.OPPONENT_DEFEATED);
        }

        notify(_currentOpponent, BattleObserver.BattleEvent.PLAYER_TURN_DONE);
    }

    public void opponentAttacks(){
        if( _currentOpponent == null ){
            return;
        }

        int currentOpponentAP = Integer.parseInt(_currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_ATTACK_POINTS.toString()));
        int damage = MathUtils.clamp(currentOpponentAP - _currentPlayerDP, 0, currentOpponentAP);
        int hpVal = ProfileManager.getInstance().getProperty("currentPlayerHP", Integer.class);
        hpVal = MathUtils.clamp( hpVal - damage, 0, hpVal);
        ProfileManager.getInstance().setProperty("currentPlayerHP", hpVal);
        notify(_currentOpponent, BattleObserver.BattleEvent.PLAYER_HIT_DAMAGE);

        System.out.println("Player HIT for " + damage + " BY " + _currentOpponent.getEntityConfig().getEntityID() + " leaving player with HP: " + hpVal);

        notify(_currentOpponent, BattleObserver.BattleEvent.OPPONENT_TURN_DONE);
    }

    public void playerRuns(){
        //TODO FINISH
        notify(_currentOpponent, BattleObserver.BattleEvent.PLAYER_RUNNING);
    }

    @Override
    public void onNotify(String value, InventoryEvent event) {
        switch(event) {
            case UPDATED_AP:
                int apVal = Integer.valueOf(value);
                _currentPlayerAP = apVal;
                System.out.println("APVAL: " + _currentPlayerAP);
                break;
            case UPDATED_DP:
                int dpVal = Integer.valueOf(value);
                _currentPlayerDP = dpVal;
                System.out.println("DPVAL: " + _currentPlayerDP);
                break;
            default:
                break;
        }
    }

}
