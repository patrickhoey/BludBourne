package com.packtpub.libgdx.bludbourne;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	private static final String TAG = Entity.class.getSimpleName();

	private Vector2 _velocity;
	private String _entityID;

	public enum Direction {
		UP,RIGHT,DOWN,LEFT;
	}

	public Direction _direction = null;

	public Vector2 _nextPlayerPosition;
	public Vector2 _currentPlayerPosition;
	protected State _state = State.IDLE;

	public static final int FRAME_WIDTH = 16;
	public static final int FRAME_HEIGHT = 16;
	public static Rectangle boundingBox;

	public enum State {
		IDLE, WALKING
	}

	InputComponent _inputComponent;
	GraphicsComponent _graphicsComponent;
	
	public Entity(){
		initEntity();
	}
	
	public void initEntity(){
		//Gdx.app.debug(TAG, "Construction" );
		
		this._entityID = UUID.randomUUID().toString();
		this._nextPlayerPosition = new Vector2();
		this._currentPlayerPosition = new Vector2();
		this.boundingBox = new Rectangle();
		this._velocity = new Vector2(2f,2f);

		_inputComponent = new InputComponent();
		_graphicsComponent = new GraphicsComponent();
	}

	public void update(Batch batch, float delta){
		_inputComponent.update(this, delta);
		_graphicsComponent.update(batch, this, delta);

		//We want the hitbox to be at the feet for a better feel
		setBoundingBoxSize(0f, 0.5f);
	}

	public void init(float startX, float startY){
		this._currentPlayerPosition.x = startX;
		this._currentPlayerPosition.y = startY;
		
		this._nextPlayerPosition.x = startX;
		this._nextPlayerPosition.y = startY;

		//Gdx.app.debug(TAG, "Calling INIT" );
	}

	public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced){
		//Update the current bounding box
		float width;
		float height;

		float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
		float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

		if( widthReductionAmount > 0 && widthReductionAmount < 1){
			width = FRAME_WIDTH * widthReductionAmount;
		}else{
			width = FRAME_WIDTH;
		}

		if( heightReductionAmount > 0 && heightReductionAmount < 1){
			height = FRAME_HEIGHT * heightReductionAmount;
		}else{
			height = FRAME_HEIGHT;
		}


		if( width == 0 || height == 0){
			Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
		}

		//Need to account for the unitscale, since the map coordinates will be in pixels
		float minX;
		float minY;
		if( MapManager.UNIT_SCALE > 0 ) {
			minX = _nextPlayerPosition.x / MapManager.UNIT_SCALE;
			minY = _nextPlayerPosition.y / MapManager.UNIT_SCALE;
		}else{
			minX = _nextPlayerPosition.x;
			minY = _nextPlayerPosition.y;
		}

		boundingBox.set(minX, minY, width, height);
		//Gdx.app.debug(TAG, "SETTING Bounding Box: (" + minX + "," + minY + ")  width: " + width + " height: " + height);
	}

	public void dispose(){
		_inputComponent.dispose();
	}
	
	public void setState(State state){
		this._state = state;
	}
	
	public Vector2 getCurrentPosition(){
		return _currentPlayerPosition;
	}
	
	public void setCurrentPosition(float currentPositionX, float currentPositionY){
		this._currentPlayerPosition.x = currentPositionX;
		this._currentPlayerPosition.y = currentPositionY;
	}
	
	public void setNextPositionToCurrent(){
		setCurrentPosition(_nextPlayerPosition.x, _nextPlayerPosition.y);
		//Gdx.app.debug(TAG, "Setting nextPosition as Current: (" + _nextPlayerPosition.x + "," + _nextPlayerPosition.y + ")");
	}
	
	public void calculateNextPosition(Direction currentDirection, float deltaTime){
		float testX = _currentPlayerPosition.x;
		float testY = _currentPlayerPosition.y;

		//Gdx.app.debug(TAG, "calculateNextPosition:: Current Position: (" + _currentPlayerPosition.x + "," + _currentPlayerPosition.y + ")"  );
		//Gdx.app.debug(TAG, "calculateNextPosition:: Current Direction: " + _currentDirection  );
		
		_velocity.scl(deltaTime);
		
		switch (currentDirection) {
		case LEFT : 
		testX -=  _velocity.x;
		break;
		case RIGHT :
		testX += _velocity.x;
		break;
		case UP : 
		testY += _velocity.y;
		break;
		case DOWN : 
		testY -= _velocity.y;
		break;
		default:
			break;
		}
		
		_nextPlayerPosition.x = testX;
		_nextPlayerPosition.y = testY;
		
		//velocity
		_velocity.scl(1 / deltaTime);
	}

	
}
