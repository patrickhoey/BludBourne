package com.packtpub.libgdx.bludbourne;

public class DemoInputComponent extends InputComponent {
    private static final String TAG = DemoInputComponent.class.getSimpleName();

    DemoInputComponent(){
        _currentDirection = Entity.Direction.getRandomNext();
    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 1 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_MAP.toString())) {
                _currentDirection = Entity.Direction.getRandomNext();
            }
        }
    }

    @Override
    public void dispose(){

    }

    @Override
    public void update(Entity entity, float delta){
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
}
