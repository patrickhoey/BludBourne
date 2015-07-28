package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.battle.BattleObserver;
import com.packtpub.libgdx.bludbourne.battle.BattleState;

public class BattleUI extends Window implements BattleObserver {
    private AnimatedImage _image;

    private final int _enemyWidth = 160;
    private final int _enemyHeight = 160;

    private BattleState _battleState = null;

    public BattleUI(){
        super("BATTLE", Utility.STATUSUI_SKIN, "solidbackground");

        _battleState = new BattleState();
        _battleState.addObserver(this);

        _image = new AnimatedImage();
        _image.setTouchable(Touchable.disabled);

        this.add(_image).size(_enemyWidth, _enemyHeight);
        this.pack();
    }

    public void battleZoneTriggered(int battleZoneValue){
        _battleState.battleZoneEntered(battleZoneValue);
    }


    @Override
    public void onNotify(Entity entity, BattleEvent event) {
        switch(event){
            case OPPONENT_ADDED:
                _image.setAnimation(entity.getAnimation(Entity.AnimationType.IMMOBILE));
                break;
            default:
                break;
        }

    }
}
