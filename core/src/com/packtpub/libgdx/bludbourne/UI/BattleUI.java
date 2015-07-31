package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;
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
    private Label _damageValLabel = null;

    private int _currentBattleZone = 0;

    private float _origDamageValLabelY = 0;

    public BattleUI(){
        super("BATTLE", Utility.STATUSUI_SKIN, "solidbackground");

        _battleState = new BattleState();
        _battleState.addObserver(this);

        _damageValLabel = new Label("0", Utility.STATUSUI_SKIN);
        _damageValLabel.setVisible(false);

        _image = new AnimatedImage();
        _image.setTouchable(Touchable.disabled);

        Table table = new Table();
        _attackButton = new TextButton("Attack", Utility.STATUSUI_SKIN, "inventory");
        _runButton = new TextButton("Run", Utility.STATUSUI_SKIN);
        table.add(_attackButton).pad(20, 20, 20, 20);
        table.row();
        table.add(_runButton).pad(20, 20, 20, 20);

        //layout
        this.add(_damageValLabel).align(Align.left).padLeft(_enemyWidth / 2).row();
        this.add(_image).size(_enemyWidth, _enemyHeight).pad(10, 10, 10, _enemyWidth / 2);
        this.add(table);

        this.pack();

        _origDamageValLabelY = _damageValLabel.getY()+_enemyHeight;

        _attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        _battleState.playerAttacks();
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
        _currentBattleZone = battleZoneValue;
        _battleState.setCurrentOpponent(battleZoneValue);
    }

    public BattleState getCurrentState(){
        return _battleState;
    }

    @Override
    public void onNotify(Entity entity, BattleEvent event) {
        switch(event){
            case PLAYER_TURN_START:
                _attackButton.setDisabled(true);
                _attackButton.setTouchable(Touchable.disabled);
                break;
            case OPPONENT_ADDED:
                _image.setAnimation(entity.getAnimation(Entity.AnimationType.IMMOBILE));
                this.setTitle("Level " + _currentBattleZone + " " + entity.getEntityConfig().getEntityID());
                break;
            case OPPONENT_HIT_DAMAGE:
                int damage = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
                _damageValLabel.setText(String.valueOf(damage));
                _damageValLabel.setY(_origDamageValLabelY);
                _damageValLabel.setVisible(true);
                break;
            case OPPONENT_DEFEATED:
                _damageValLabel.setVisible(false);
                _damageValLabel.setY(_origDamageValLabelY);
                break;
            case OPPONENT_TURN_DONE:
                 _attackButton.setDisabled(false);
                 _attackButton.setTouchable(Touchable.enabled);
                break;
            case PLAYER_TURN_DONE:
                _battleState.opponentAttacks();
                break;
            default:
                break;
        }
    }

    @Override
    public void act(float delta){
        if( _damageValLabel.isVisible() ){
            _damageValLabel.setY(_damageValLabel.getY()+3);
        }

        super.act(delta);
    }
}
