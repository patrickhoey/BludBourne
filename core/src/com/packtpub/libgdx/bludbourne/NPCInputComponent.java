package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

public class NPCInputComponent extends InputComponent {
    private static final String TAG = NPCInputComponent.class.getSimpleName();

    private float _frameTime = 0.0f;

    NPCInputComponent(){
        _currentDirection = Entity.Direction.getRandomNext();
        _currentState = Entity.State.WALKING;
    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 1 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_MAP.toString())) {
                _currentDirection = Entity.Direction.getRandomNext();
            }else if (string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_ENTITY.toString())) {
                _currentState = Entity.State.IDLE;
                //_currentDirection = _currentDirection.getOpposite();
            }
        }

        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_STATE.toString())) {
                _currentState = _json.fromJson(Entity.State.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAGE.INIT_DIRECTION.toString())) {
                _currentDirection = _json.fromJson(Entity.Direction.class, string[1]);
            }
        }

    }

    @Override
    public void dispose(){

    }

    @Override
    public void update(Entity entity, float delta){
        if(keys.get(Keys.QUIT)) {
            Gdx.app.exit();
        }

        //If IMMOBILE, don't update anything
        if( _currentState == Entity.State.IMMOBILE ) {
            entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.IMMOBILE));
            return;
        }

        _frameTime += delta;

        //Change direction after so many seconds
        if( _frameTime > MathUtils.random(1,5) ){
            _currentState = Entity.State.getRandomNext();
            _currentDirection = Entity.Direction.getRandomNext();
            _frameTime = 0.0f;
        }

        if( _currentState == Entity.State.IDLE ){
            entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.IDLE));
            return;
        }

        switch( _currentDirection ) {
            case LEFT:
                entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, _json.toJson(Entity.Direction.LEFT));
                break;
            case RIGHT:
                entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, _json.toJson(Entity.Direction.RIGHT));
                break;
            case UP:
                entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, _json.toJson(Entity.Direction.UP));
                break;
            case DOWN:
                entity.sendMessage(MESSAGE.CURRENT_STATE, _json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, _json.toJson(Entity.Direction.DOWN));
                break;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if( keycode == Input.Keys.Q){
            keys.put(Keys.QUIT, true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
