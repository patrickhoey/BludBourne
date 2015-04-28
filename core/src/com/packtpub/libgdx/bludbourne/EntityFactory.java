package com.packtpub.libgdx.bludbourne;


public class EntityFactory {

    public static enum EntityType{
        PLAYER
    }

    static public Entity getEntity(EntityType entityType){

        switch(entityType){
            case PLAYER:
                return new Entity(new PlayerInputComponent(), new PlayerPhysicsComponent(), new PlayerGraphicsComponent());
            default:
                return null;
        }
    }
}
