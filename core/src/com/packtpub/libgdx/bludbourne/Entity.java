package com.packtpub.libgdx.bludbourne;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Entity {
	
	private static final String TAG = Entity.class.getSimpleName();
	private static final String _defaultSpritePath = "sprites/characters/Warrior.png";

	private Vector2 _velocity;
	private String _entityID;

	private Direction _currentDirection = Direction.LEFT;
	private Direction _previousDirection = Direction.UP;

	private Animation _walkLeftAnimation;
	private Animation _walkRightAnimation;
	private Animation _walkUpAnimation;
	private Animation _walkDownAnimation;

	private Array<TextureRegion> _walkLeftFrames;
	private Array<TextureRegion> _walkRightFrames;
	private Array<TextureRegion> _walkUpFrames;
	private Array<TextureRegion> _walkDownFrames;

	protected Vector2 _nextPlayerPosition;
	protected Vector2 _currentPlayerPosition;
	protected State _state = State.IDLE;
	protected float _frameTime = 0f;
	protected Sprite _frameSprite = null;
	protected TextureRegion _currentFrame = null;

	public final int FRAME_WIDTH = 16;
	public final int FRAME_HEIGHT = 16;
	public static Rectangle boundingBox;

	public enum State {
		IDLE, WALKING
	}
	
	public enum Direction {
		UP,RIGHT,DOWN,LEFT;
	}
	
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

		Utility.loadTextureAsset(_defaultSpritePath);
		loadDefaultSprite();
		loadAllAnimations();
	}

	public void update(float delta){
		_frameTime = (_frameTime + delta)%5; //Want to avoid overflow

		//Gdx.app.debug(TAG, "frametime: " + _frameTime );

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

	private void loadDefaultSprite()
	{
		Texture texture = Utility.getTextureAsset(_defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
		_frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0,0,FRAME_WIDTH, FRAME_HEIGHT);
		_currentFrame = textureFrames[0][0];
	}
	
	private void loadAllAnimations(){
		//Walking animation
		Texture texture = Utility.getTextureAsset(_defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);

		_walkDownFrames = new Array<TextureRegion>(4);
		_walkLeftFrames = new Array<TextureRegion>(4);
		_walkRightFrames = new Array<TextureRegion>(4);
		_walkUpFrames = new Array<TextureRegion>(4);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				//Gdx.app.debug(TAG, "Got frame " + i + "," + j + " from " + sourceImage);
				TextureRegion region = textureFrames[i][j];
				if( region == null ){
					Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
				}
				switch(i)
				{
					case 0:
						_walkDownFrames.insert(j, region);
						break;
					case 1:
						_walkLeftFrames.insert(j, region);
						break;
					case 2:
						_walkRightFrames.insert(j, region);
						break;
					case 3:
						_walkUpFrames.insert(j, region);
						break;
				}
			}
		}


		_walkDownAnimation = new Animation(0.25f, _walkDownFrames, Animation.PlayMode.LOOP);
		_walkLeftAnimation = new Animation(0.25f, _walkLeftFrames, Animation.PlayMode.LOOP);
		_walkRightAnimation = new Animation(0.25f, _walkRightFrames, Animation.PlayMode.LOOP);
		_walkUpAnimation = new Animation(0.25f, _walkUpFrames, Animation.PlayMode.LOOP);
	}

	public void dispose(){
		Utility.unloadAsset(_defaultSpritePath);
	}
	
	public void setState(State state){
		this._state = state;
	}
	
	public Sprite getFrameSprite(){
		return _frameSprite;
	}

	public TextureRegion getFrame(){
		return _currentFrame;
	}
	
	public Vector2 getCurrentPosition(){
		return _currentPlayerPosition;
	}
	
	public void setCurrentPosition(float currentPositionX, float currentPositionY){
		_frameSprite.setX(currentPositionX);
		_frameSprite.setY(currentPositionY);
		this._currentPlayerPosition.x = currentPositionX;
		this._currentPlayerPosition.y = currentPositionY;
	}
	
	public void setDirection(Direction direction,  float deltaTime){
		this._previousDirection = this._currentDirection;
		this._currentDirection = direction;
		
		//Look into the appropriate variable when changing position

		switch (_currentDirection) {
		case DOWN :
			_currentFrame = _walkDownAnimation.getKeyFrame(_frameTime);
			break;
		case LEFT :
			_currentFrame = _walkLeftAnimation.getKeyFrame(_frameTime);
			break;
		case UP :
			_currentFrame = _walkUpAnimation.getKeyFrame(_frameTime);
			break;
		case RIGHT :
			_currentFrame = _walkRightAnimation.getKeyFrame(_frameTime);
			break;
		default:
			break;
		}
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
