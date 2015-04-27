package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Entity {
	private static final String TAG = Entity.class.getSimpleName();

	public static enum Direction {
		UP,RIGHT,DOWN,LEFT;
	}

	public static enum State {
		IDLE, WALKING
	}

	public static final int FRAME_WIDTH = 16;
	public static final int FRAME_HEIGHT = 16;

	private static final int MAX_COMPONENTS = 5;
	private Array<Component> _components;

	private InputComponent _inputComponent;
	private GraphicsComponent _graphicsComponent;
	private PhysicsComponent _physicsComponent;
	
	public Entity(){
		_components = new Array<Component>(MAX_COMPONENTS);

		_inputComponent = new InputComponent();
		_components.add(_inputComponent);

		_graphicsComponent = new GraphicsComponent();
		_components.add(_graphicsComponent);

		_physicsComponent = new PhysicsComponent();
		_components.add(_physicsComponent);
	}

	public void sendMessage(Component.MESSAGE messageType, String ... args){
		String fullMessage = messageType.toString();

		for (String string : args) {
			fullMessage += Component.MESSAGE_TOKEN + string;
		}

		for(Component component: _components){
			component.receiveMessage(fullMessage);
		}
	}

	public void update(MapManager mapMgr, Batch batch, float delta){
		_inputComponent.update(this, delta);
		_physicsComponent.update(this, mapMgr, delta);
		_graphicsComponent.update(this, batch, delta);
	}

	public void dispose(){
		for(Component component: _components){
			component.dispose();
		}
	}
}
