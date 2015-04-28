package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.utils.Json;

public abstract class InputComponent implements Component {

    protected Entity.Direction _currentDirection = null;
    protected Json _json;

    InputComponent(){
        _json = new Json();
    }

    public abstract void update(Entity entity, float delta);

}
