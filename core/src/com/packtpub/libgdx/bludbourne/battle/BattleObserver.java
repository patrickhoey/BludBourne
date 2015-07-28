package com.packtpub.libgdx.bludbourne.battle;

import com.packtpub.libgdx.bludbourne.Entity;

public interface BattleObserver {
    public static enum BattleEvent{
        OPPONENT_ADDED,
        NONE
    }

    void onNotify(final Entity enemyEntity, BattleEvent event);
}
