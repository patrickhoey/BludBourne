package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class GraphicsComponent implements Component {

    public abstract void update(Entity entity, Batch batch, float delta);

}
