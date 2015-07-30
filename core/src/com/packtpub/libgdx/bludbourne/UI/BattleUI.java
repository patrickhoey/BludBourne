package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.battle.BattleObserver;
import com.packtpub.libgdx.bludbourne.battle.BattleState;

public class BattleUI extends Window implements BattleObserver {
    private AnimatedImage _image;

    private final int _enemyWidth = 160;
    private final int _enemyHeight = 160;

    private BattleState _battleState = null;
    private TextButton _attackButton = null;
    private TextButton _runButton = null;

    public BattleUI(){
        super("BATTLE", Utility.STATUSUI_SKIN, "solidbackground");

        _battleState = new BattleState();
        _battleState.addObserver(this);

        _image = new AnimatedImage();
        _image.setTouchable(Touchable.disabled);

        Table table = new Table();
        _attackButton = new TextButton("Attack", Utility.STATUSUI_SKIN);
        _runButton = new TextButton("Run", Utility.STATUSUI_SKIN);
        table.add(_attackButton).pad(20,20,20,20);
        table.row();
        table.add(_runButton).pad(20,20,20,20);

        //layout
        this.add(_image).size(_enemyWidth, _enemyHeight).pad(20, 20, 20, _enemyWidth / 2);
        this.add(table);

        this.pack();

        _attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        _battleState.playerAttacks();
                        //_attackButton.setDisabled(true);
                        //_attackButton.setTouchable(Touchable.disabled);
                    }
                }
        );
        _runButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        _battleState.playerRuns();
                    }
                }
        );
    }

    public void battleZoneTriggered(int battleZoneValue){
        _battleState.setCurrentOpponent(battleZoneValue);
    }

    public BattleState getCurrentState(){
        return _battleState;
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
