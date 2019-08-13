package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.battle.BattleObserver;
import com.packtpub.libgdx.bludbourne.battle.BattleState;
import com.packtpub.libgdx.bludbourne.sfx.ParticleEffectFactory;
import com.packtpub.libgdx.bludbourne.sfx.ShakeCamera;

public class BattleUI extends Window implements BattleObserver {
    private static final String TAG = BattleUI.class.getSimpleName();

    private AnimatedImage _image;

    private final int _enemyWidth = 96;
    private final int _enemyHeight = 96;

    private BattleState _battleState = null;
    private TextButton _attackButton = null;
    private TextButton _runButton = null;
    private Label _damageValLabel = null;

    private float _battleTimer = 0;
    private final float _checkTimer = 1;

    private ShakeCamera _battleShakeCam = null;
    private Array<ParticleEffect> _effects;

    private float _origDamageValLabelY = 0;
    private Vector2 _currentImagePosition;

    public BattleUI(){
        super("BATTLE", Utility.STATUSUI_SKIN, "solidbackground");

        _battleTimer = 0;
        _battleState = new BattleState();
        _battleState.addObserver(this);

        _effects = new Array<ParticleEffect>();
        _currentImagePosition = new Vector2(0,0);

        _damageValLabel = new Label("0", Utility.STATUSUI_SKIN);
        _damageValLabel.setVisible(false);

        _image = new AnimatedImage();
        _image.setTouchable(Touchable.disabled);

        Table table = new Table();
        _attackButton = new TextButton("Attack", Utility.STATUSUI_SKIN, "inventory");
        _runButton = new TextButton("Run", Utility.STATUSUI_SKIN, "inventory");
        table.add(_attackButton).pad(20, 20, 20, 20);
        table.row();
        table.add(_runButton).pad(20, 20, 20, 20);

        //layout
        this.setFillParent(true);
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
        _battleState.setCurrentZoneLevel(battleZoneValue);
    }

    public boolean isBattleReady(){
        if( _battleTimer > _checkTimer ){
            _battleTimer = 0;
            return _battleState.isOpponentReady();
        }else{
            return false;
        }
    }

    public BattleState getCurrentState(){
        return _battleState;
    }

    @Override
    public void onNotify(Entity entity, BattleEvent event) {
        switch(event){
            case PLAYER_TURN_START:
                _runButton.setDisabled(true);
                _runButton.setTouchable(Touchable.disabled);
                _attackButton.setDisabled(true);
                _attackButton.setTouchable(Touchable.disabled);
                break;
            case OPPONENT_ADDED:
                _image.setEntity(entity);
                _image.setCurrentAnimation(Entity.AnimationType.IMMOBILE);
                _image.setSize(_enemyWidth, _enemyHeight);
                _image.setPosition(this.getCell(_image).getActorX(), this.getCell(_image).getActorY());

                _currentImagePosition.set(_image.getX(), _image.getY());
                if( _battleShakeCam == null ){
                    _battleShakeCam = new ShakeCamera(_currentImagePosition.x, _currentImagePosition.y, 30.0f);
                }

                //Gdx.app.debug(TAG, "Image position: " + _image.getX() + "," + _image.getY() );

                this.getTitleLabel().setText("Level " + _battleState.getCurrentZoneLevel() + " " + entity.getEntityConfig().getEntityID());
                break;
            case OPPONENT_HIT_DAMAGE:
                int damage = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
                _damageValLabel.setText(String.valueOf(damage));
                _damageValLabel.setY(_origDamageValLabelY);
                _battleShakeCam.startShaking();
                _damageValLabel.setVisible(true);
                break;
            case OPPONENT_DEFEATED:
                _damageValLabel.setVisible(false);
                _damageValLabel.setY(_origDamageValLabelY);
                break;
            case OPPONENT_TURN_DONE:
                 _attackButton.setDisabled(false);
                 _attackButton.setTouchable(Touchable.enabled);
                _runButton.setDisabled(false);
                _runButton.setTouchable(Touchable.enabled);
                break;
            case PLAYER_TURN_DONE:
                _battleState.opponentAttacks();
                break;
            case PLAYER_USED_MAGIC:
                float x = _currentImagePosition.x + (_enemyWidth/2);
                float y = _currentImagePosition.y + (_enemyHeight/2);
                _effects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.WAND_ATTACK, x,y));
                break;
            default:
                break;
        }
    }

    public void resize() {
        _image.setPosition(this.getCell(_image).getActorX(), this.getCell(_image).getActorY());
        _currentImagePosition.set(_image.getX(), _image.getY());

        Gdx.app.debug(TAG, "RESIZE Image position: " + _image.getX() + "," + _image.getY());

        if( _battleShakeCam != null ){
            _battleShakeCam.setOrigPosition(_currentImagePosition.x, _currentImagePosition.y);
            _battleShakeCam.reset();
        }
    }

    public void resetDefaults(){
        _battleTimer = 0;
        _battleState.resetDefaults();
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        //Draw the particles last
        for( int i = 0; i < _effects.size; i++){
            ParticleEffect effect = _effects.get(i);
            if( effect == null ) continue;
            effect.draw(batch);
        }
    }

    @Override
    public void act(float delta){
        _battleTimer = (_battleTimer + delta)%60;
        if( _damageValLabel.isVisible() && _damageValLabel.getY() < this.getHeight()){
            _damageValLabel.setY(_damageValLabel.getY()+5);
        }

        if( _battleShakeCam != null && _battleShakeCam.isCameraShaking() ){
            Vector2 shakeCoords = _battleShakeCam.getNewShakePosition();
            _image.setPosition(shakeCoords.x, shakeCoords.y);
        }

        for( int i = 0; i < _effects.size; i++){
            ParticleEffect effect = _effects.get(i);
            if( effect == null ) continue;
            if( effect.isComplete() ){
                _effects.removeIndex(i);
                effect.dispose();
            }else{
                effect.update(delta);
            }
        }

        super.act(delta);
    }
}
