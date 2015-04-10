package com.packtpub.libgdx.bludbourne;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Entity {
	
	private static final String TAG = Entity.class.getSimpleName();
	private static final String defaultSpritePath = "sprites/characters/Warrior.png";

	private Vector2 velocity;

	public int FRAME_WIDTH = 16;
	public int FRAME_HEIGHT = 16;
	
	private String entityID;
	
	protected Rectangle boundingBox;


	protected State state = State.IDLE;

	protected float frameTime = 0f;
	protected Sprite frameSprite = null;
	protected TextureRegion currentFrame = null;
	private Direction currentDirection = Direction.LEFT;
	private Direction previousDirection = Direction.UP;

	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;

	private Array<TextureRegion> walkLeftFrames;
	private Array<TextureRegion> walkRightFrames;
	private Array<TextureRegion> walkUpFrames;
	private Array<TextureRegion> walkDownFrames;

	protected float rotationDegrees = 0;
	protected Vector2 nextPlayerPosition;
	protected Vector2 currentPlayerPosition;

	public enum State {
		IDLE, WALKING
	}
	
	public enum Direction {
		UP,RIGHT,DOWN,LEFT;
		
		public Direction getNext() {
			//Gdx.app.debug(TAG, "Current Direction: " + Direction.values()[(ordinal()) % Direction.values().length] );
			//Gdx.app.debug(TAG, "Current Direction: " + ordinal() );
			//Gdx.app.debug(TAG, "Next Direction: " + Direction.values()[(ordinal()+1) % Direction.values().length] );
			return Direction.values()[(ordinal()+1) % Direction.values().length];
		}
		
		public Direction getRandomNext() {
			return Direction.values()[MathUtils.random(Direction.values().length-1)];
		}
		
		public Direction getOpposite() {
			if( this == LEFT){
				return RIGHT;
			}else if( this == RIGHT){
				return LEFT;
			}else if( this == UP){
				return DOWN;
			}else{
				return UP;
			}
		}
		
	}
	
	public Entity(){
		initEntity();
	}
	
	public Entity(String entityType){
		initEntity();
		/*
		entityScript = ScriptManager.getInstance().scriptFactory(entityType);
		if( entityScript != null ){
			entityScript.create(this);
		}
		*/
	}
	
	public void initEntity(){
		//Gdx.app.debug(TAG, "Construction" );
		
		this.entityID = UUID.randomUUID().toString();
		this.nextPlayerPosition = new Vector2();
		this.currentPlayerPosition = new Vector2();
		this.boundingBox = new Rectangle();
		this.velocity = new Vector2(2f,2f);

		Utility.loadTextureAsset(defaultSpritePath);
		loadDefaultSprite();
		loadAllAnimations();
	}


	public String getEntityID(){
		return entityID;
	}

	public Vector2 getVelocity(){
		return new Vector2(velocity);
	}
	public void setVelocity(Vector2 velocity){
		this.velocity = velocity;
	}

	public void update(float delta){
		frameTime += delta;
	}

	public void init(float startX, float startY){
		this.currentPlayerPosition.x = startX;
		this.currentPlayerPosition.y = startY;
		
		this.nextPlayerPosition.x = startX;
		this.nextPlayerPosition.y = startY;

		//Gdx.app.debug(TAG, "Calling INIT" );
	}

	public Rectangle getEntityBoundingBox(){
		//Update the current bounding box
		if( frameSprite == null ){
			Gdx.app.debug(TAG, "Framesprite for getEntityBoundingBox() is NULL");
			return boundingBox;
		}

		//Gdx.app.debug(TAG, "GETTING Bounding Box: " + boundingBox.getX() + "," + boundingBox.getY() + "width " + boundingBox.getWidth() + " height " + boundingBox.getHeight());

		return boundingBox;
	}

	public void setBoundingBoxSize(float percentageReduced){
		//Update the current bounding box
		float width;
		float height;
		float xOffset;
		float yOffset;

		float reductionAmount = 1.0f - percentageReduced; //.8f for 20% (1 - .20)

		if( reductionAmount > 0 && reductionAmount < 1){
			width = FRAME_WIDTH * reductionAmount; //reduce by 20%
			height = FRAME_HEIGHT * reductionAmount; //reduce by 20%
		}else{
			width = FRAME_WIDTH;
			height = FRAME_HEIGHT;
		}

		if( width == 0 || height == 0){
			Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
		}

		xOffset = (FRAME_WIDTH - width)/2;
		yOffset =  (FRAME_HEIGHT - height)/2;

		//Gdx.app.debug(TAG, "Reduction amount: " + width + ":" + height);
		//Gdx.app.debug(TAG, "Regular amount: " + WIDTH + ":" + HEIGHT);
		//Gdx.app.debug(TAG, "Offset amount: " + xOffset + "," + yOffset);

		float minX = nextPlayerPosition.x + xOffset;
		float minY = nextPlayerPosition.y + yOffset;

		boundingBox.set( minX,minY,width,height);
		//Gdx.app.debug(TAG, "SETTING Bounding Box: " + minX + "," + minY + "width " + width + " height " + height);
	}

	private void loadDefaultSprite()
	{
		Texture texture = Utility.getTextureAsset(defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
		frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0,0,FRAME_WIDTH, FRAME_HEIGHT);
		currentFrame = textureFrames[0][0];
	}
	
	private void loadAllAnimations(){
		//Walking animation
		Texture texture = Utility.getTextureAsset(defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);

		walkDownFrames = new Array<TextureRegion>(4);
		walkLeftFrames = new Array<TextureRegion>(4);
		walkRightFrames = new Array<TextureRegion>(4);
		walkUpFrames = new Array<TextureRegion>(4);

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
						walkDownFrames.insert(j,region);
						break;
					case 1:
						walkLeftFrames.insert(j,region);
						break;
					case 2:
						walkRightFrames.insert(j,region);
						break;
					case 3:
						walkUpFrames.insert(j,region);
						break;
				}
			}
		}


		walkDownAnimation = new Animation(0.25f, walkDownFrames, Animation.PlayMode.LOOP);
		walkLeftAnimation = new Animation(0.25f, walkLeftFrames, Animation.PlayMode.LOOP);
		walkRightAnimation = new Animation(0.25f, walkRightFrames, Animation.PlayMode.LOOP);
		walkUpAnimation = new Animation(0.25f, walkUpFrames, Animation.PlayMode.LOOP);
	}

	public void dispose(){
		//Utility.unloadAsset(imagePath);
	}
	
	public void setState(State state){
		this.state = state;
	}
	
	public State getState(){
		return state;
	}
	
	public Sprite getFrameSprite(){
		return frameSprite;
	}

	public TextureRegion getFrame(){
		return currentFrame;
	}
	
	public Vector2 getNextPosition(){
		return nextPlayerPosition;
	}
	
	public void setNextPosition(float nextPositionX, float nextPositionY){
		this.nextPlayerPosition.x = nextPositionX;
		this.nextPlayerPosition.y = nextPositionY;
	}
	
	public Vector2 getCurrentPosition(){
		return currentPlayerPosition;
	}
	
	public void setCurrentPosition(float currentPositionX, float currentPositionY){
		this.currentPlayerPosition.x = currentPositionX;
		this.currentPlayerPosition.y = currentPositionY;
	}
	
	public void setDirection(Direction direction,  float deltaTime){
		this.previousDirection = this.currentDirection;
		this.currentDirection = direction;
		
		//Look into the appropriate variable when changing position

		switch (currentDirection) {
		case DOWN :
			currentFrame = walkDownAnimation.getKeyFrame(frameTime);
			break;
		case LEFT :
			currentFrame = walkLeftAnimation.getKeyFrame(frameTime);
			break;
		case UP :
			currentFrame = walkUpAnimation.getKeyFrame(frameTime);
			break;
		case RIGHT :
			currentFrame = walkRightAnimation.getKeyFrame(frameTime);
			break;
		default:
			break;
		}
	}

	public Direction getCurrentDirection(){
		return currentDirection;
	}

	public Direction getPreviousDirection(){
		return previousDirection;
	}

	
	public void setNextPositionToCurrent(){
		//if( state == State.PAUSE){
		//	return;
		//}

		frameSprite.setX(nextPlayerPosition.x);
		frameSprite.setY(nextPlayerPosition.y);
		setCurrentPosition(nextPlayerPosition.x, nextPlayerPosition.y);
		Gdx.app.debug(TAG, "Setting nextPosition as Current: (" + nextPlayerPosition.x + "," + nextPlayerPosition.y + ")");
	}
	
	
	public void calculateNextPosition(Direction currentDirection, float deltaTime){
		//if( state == State.PAUSE){
		//	return;
		//}


		float testX = currentPlayerPosition.x;
		float testY = currentPlayerPosition.y;


		//Gdx.app.debug(TAG, "calculateNextPosition:: Current Position: (" + currentPlayerPosition.x + "," + currentPlayerPosition.y + ")"  );
		//Gdx.app.debug(TAG, "calculateNextPosition:: Current Direction: " + currentDirection  );
		
		velocity.scl(deltaTime);
		
		switch (currentDirection) {
		case LEFT : 
		testX -=  velocity.x;
		break;
		case RIGHT :
		testX += velocity.x;
		break;
		case UP : 
		testY += velocity.y;
		break;
		case DOWN : 
		testY -= velocity.y;
		break;
		default:
			break;
		}
		
		nextPlayerPosition.x = testX;
		nextPlayerPosition.y = testY;
		
		//velocity
		velocity.scl(1/deltaTime);		
	}

	
}
