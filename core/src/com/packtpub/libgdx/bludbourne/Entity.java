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

	protected Sprite frameSprite;
	private Direction currentDirection = Direction.LEFT;
	private Direction previousDirection = Direction.UP;

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

		//loadTextures();

		/*
		TextureRegion currentFrame = getCurrentFrame(delta);

		if( currentFrame == null || frameSprite == null){
			Gdx.app.debug(TAG, "Sprite/currentFrame is null for nextPosition" );
			return;
		}

		//Gdx.app.debug(TAG, "Current Region Width: " + currentFrame.getRegionWidth() + " and height: " + currentFrame.getRegionHeight()  );

		frameSprite.setRegion(currentFrame);

		//Gdx.app.debug(TAG, "FrameSprite Region Width: " + frameSprite.getRegionWidth() + " and height: " + frameSprite.getRegionHeight()  );
        */
	}

	/*
	public TextureRegion getCurrentFrame(float delta){

		if( walkAnimation == null ){
			Gdx.app.debug(TAG, "Current animation is NULL..." );
			return null;
		}

		TextureRegion currentFrame = null;

		if( state == State.WALKING ){
			currentFrame = walkAnimation.getKeyFrame(frameTime, false);
		}else if( state == State.ANIMATED){
			if( (walkAnimation.getPlayMode() == Animation.PlayMode.NORMAL ||
					walkAnimation.getPlayMode() == Animation.PlayMode.REVERSED) &&
					walkAnimation.isAnimationFinished(frameTime))
			{
				//If we are playing once (normal or reversed) and we are done, set to idle
				state = State.IDLE;
				walkAnimation.setPlayMode(Animation.PlayMode.NORMAL);
				currentFrame = walkAnimation.getKeyFrame(idleKeyFrame, false);
			}else{
				currentFrame = walkAnimation.getKeyFrame(frameTime, false);
			}
		}else if( state == State.ANIMATE_ONCE){
			walkAnimation.setPlayMode(Animation.PlayMode.NORMAL);
			frameTime = 0f;
			state = State.ANIMATED;
			currentFrame = walkAnimation.getKeyFrame(frameTime, false);
		}else if( state == State.ANIMATE_ONCE_REVERSE ){
			walkAnimation.setPlayMode(Animation.PlayMode.REVERSED);
			frameTime = 0f;
			state = State.ANIMATED;
			currentFrame = walkAnimation.getKeyFrame(frameTime, false);
		}else if( state == State.IDLE ){
			currentFrame = walkAnimation.getKeyFrame(idleKeyFrame, false);
		}

		return currentFrame;
	}*/

	/*
	public int getCurrentFrameIndex(){
		int keyFrameIndex = -1;

		if( walkAnimation == null ){
			Gdx.app.debug(TAG, "Current animation is NULL..." );
			return keyFrameIndex;
		}

		if( state == State.WALKING || state == State.ANIMATED){
			keyFrameIndex = walkAnimation.getKeyFrameIndex(frameTime);
		}else if( state == State.IDLE ){
			keyFrameIndex = 0;
		}

		return keyFrameIndex;
	}*/

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
	}
	
	private void loadTextures(){
		//Walking animation
		/*
		if(	walkingCycle == null && Utility.isAssetLoaded(imagePath)){
			walkingCycle = Utility.getTextureAsset(imagePath);
			loadedWalkAnimationImagePath = imagePath;
			
			if( walkingCycle == null ){
				Gdx.app.debug(TAG, "Walking Texture is null" );
				return;
			}
			
			TextureRegion[] walkCycleFrames = getFramesfromImage(walkingCycle);


	        walkAnimation = new Animation(0.11f, walkCycleFrames);
	        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
	        
	        //get the first frame so we can render something
	        TextureRegion currentFrame = walkAnimation.getKeyFrame(idleKeyFrame, false);
	        
			if( currentFrame == null ){
				Gdx.app.debug(TAG, "Current frame is null" );
				return;
			}
			
	        frameSprite.setRegion(currentFrame);
	        frameSprite.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
	        frameSprite.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	        
	        //We are doing a 1 pixel for every unit for the game
	        WIDTH = 1f * currentFrame.getRegionWidth();
	        HEIGHT = 1f * currentFrame.getRegionHeight();

	        //Now that the Height and Width are set, we need to set the boundingbox
	        setBoundingBoxSize(0f);

		}
		*/
	}

	/*
	public void loadWalkingAnimation(int numRows, int numColumns, int totalFrames)
	{
		walkingAnimStartRowIndex = 0;
		walkingAnimStartColIndex = 0;
		
		initWalkingAnim(numRows, numColumns, totalFrames);
	}
	
	public void loadWalkingAnimation(int startRowIndex, int startColIndex, int numRows, int numColumns, int totalFrames)
	{
		walkingAnimStartRowIndex = startRowIndex;
		walkingAnimStartColIndex = startColIndex;
		
		initWalkingAnim(numRows, numColumns, totalFrames);
	}
	
	private void initWalkingAnim(int numRows, int numColumns, int totalFrames){
		walkingAnimRows = numRows;
		walkingAnimCols = numColumns;
		walkingAnimFrames = totalFrames;
		
		Utility.unloadAsset(loadedWalkAnimationImagePath);
		walkingCycle = null;
		
		Utility.loadTextureAsset(imagePath);
	}*/
	
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

    /*
	protected TextureRegion[] getFramesfromImage(Texture sourceImage){	
		//Handle walking animation of main character
		final int sourceCycleRow = walkingAnimRows;
		final int sourceCycleCol = walkingAnimCols;

		int frameWidth = sourceImage.getWidth()  / sourceCycleCol;
		int frameHeight = sourceImage.getHeight() / sourceCycleRow;
		
		TextureRegion[][] temp = TextureRegion.split(sourceImage, frameWidth, frameHeight);  
		
		TextureRegion[] textureFrames = new TextureRegion[walkingAnimFrames];

		int index = 0;
        for (int i = walkingAnimStartRowIndex; i < sourceCycleRow && index < walkingAnimFrames; i++) {
                for (int j = walkingAnimStartColIndex; j < sourceCycleCol && index < walkingAnimFrames; j++) {
                	//Gdx.app.debug(TAG, "Got frame " + i + "," + j + " from " + sourceImage);
                	TextureRegion region = temp[i][j];
                	if( region == null ){
                		Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j + " from " + sourceImage);
                	}
                	textureFrames[index] = region;
                	index++;
                }
        }
        
        return textureFrames;
	}
	*/
	
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
	
	public void setDirection(Direction direction){
		this.previousDirection = this.currentDirection;
		this.currentDirection = direction;
		
		//Look into the appropriate variable when changing position

		switch (currentDirection) {
		case DOWN : rotationDegrees = 0;
		break;
		case LEFT : rotationDegrees = 270;
		break;
		case UP : rotationDegrees = 180;
		break;
		case RIGHT : rotationDegrees = 90;
		break;
		default:
			break;
		}
		
		//frameSprite.setRotation(rotationDegrees);
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
		Gdx.app.debug(TAG, "Setting nextPosition as Current: (" + nextPlayerPosition.x + "," + nextPlayerPosition.y + ")"  );
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
