package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	private static final String TAG = Entity.class.getSimpleName();

	public enum Direction {
		UP,RIGHT,DOWN,LEFT;
	}

	public Direction _direction = null;

	protected State _state = State.IDLE;

	public Vector2 _currentPlayerPosition;
	public Vector2 _nextPlayerPosition;
	public Rectangle _boundingBox;

	public static final int FRAME_WIDTH = 16;
	public static final int FRAME_HEIGHT = 16;

	public enum State {
		IDLE, WALKING
	}

	private InputComponent _inputComponent;
	private GraphicsComponent _graphicsComponent;
	private PhysicsComponent _physicsComponent;
	
	public Entity(){
		_inputComponent = new InputComponent();
		_graphicsComponent = new GraphicsComponent();
		_physicsComponent = new PhysicsComponent();
	}

	public void update(MapManager mapMgr, Batch batch, float delta){
		_inputComponent.update(this, delta);

		_physicsComponent.update(mapMgr, this, delta);

		_currentPlayerPosition = _physicsComponent._currentPlayerPosition;
		_nextPlayerPosition = _physicsComponent._nextPlayerPosition;
		_boundingBox = _physicsComponent._boundingBox;

		_graphicsComponent.update(batch, this, delta);

	}

	public void dispose(){
		_inputComponent.dispose();
		_graphicsComponent.dispose();
		_physicsComponent.dispose();
	}
	
	public void setState(State state){
		this._state = state;
	}
}
