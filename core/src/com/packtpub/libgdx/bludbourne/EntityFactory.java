package com.packtpub.libgdx.bludbourne;


public class EntityFactory {

    public static enum EntityType{
        PLAYER,
        DEMO_PLAYER,
        NPC
    }

    static public Entity getEntity(EntityType entityType){

        switch(entityType){
            case PLAYER:
                return new Entity(new PlayerInputComponent(), new PlayerPhysicsComponent(), new PlayerGraphicsComponent());
            case DEMO_PLAYER:
                return new Entity(new NPCInputComponent(), new PlayerPhysicsComponent(), new PlayerGraphicsComponent());
            case NPC:
                return new Entity(new NPCInputComponent(), new NPCPhysicsComponent(), new NPCGraphicsComponent());
            default:
                return null;
        }
    }
}