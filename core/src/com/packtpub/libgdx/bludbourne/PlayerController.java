package com.packtpub.libgdx.bludbourne;


import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;


public class PlayerController {

	private final static String TAG = PlayerController.class.getSimpleName();
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN, QUIT
	}
	
	enum Mouse {
		SELECT, DOACTION
	}
	
	static Map<Keys, Boolean> keys = new HashMap<PlayerController.Keys, Boolean>();
	static Map<Mouse, Boolean> mouseButtons = new HashMap<PlayerController.Mouse, Boolean>();
	private Vector3 lastMouseCoordinates;
	
	//initialize the hashmap for inputs
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	};
	
	static {
		mouseButtons.put(Mouse.SELECT, false);
		mouseButtons.put(Mouse.DOACTION, false);
	};
	
	public PlayerController(){
		//Gdx.app.debug(TAG, "Construction" );		
		this.lastMouseCoordinates = new Vector3();
	}
	
	public void dispose(){
		
	}
	
	//Key presses
	public void leftPressed(){
		keys.put(Keys.LEFT, true);
	}
	
	public void rightPressed(){
		keys.put(Keys.RIGHT, true);
	}
	
	public void upPressed(){
		keys.put(Keys.UP, true);
	}
	
	public void downPressed(){
		keys.put(Keys.DOWN, true);
	}
	public void quitPressed(){
		keys.put(Keys.QUIT, true);
	}
	
	public void setClickedMouseCoordinates(int x,int y){
		lastMouseCoordinates.set(x, y, 0);
	}
	
	public void selectMouseButtonPressed(int x, int y){
		mouseButtons.put(Mouse.SELECT, true);
	}
	
	public void doActionMouseButtonPressed(int x, int y){
		mouseButtons.put(Mouse.DOACTION, true);
	}
	
	//Releases
	
	public void leftReleased(){
		keys.put(Keys.LEFT, false);
	}
	
	public void rightReleased(){
		keys.put(Keys.RIGHT, false);
	}
	
	public void upReleased(){
		keys.put(Keys.UP, false);
	}
	
	public void downReleased(){
		keys.put(Keys.DOWN, false);
	}
	
	public void quitReleased(){
		keys.put(Keys.QUIT, false);
	}
	
	public void selectMouseButtonReleased(int x, int y){
		mouseButtons.put(Mouse.SELECT, false);
	}
	
	public void doActionMouseButtonReleased(int x, int y){
		mouseButtons.put(Mouse.DOACTION, false);
	}
	
	
	public void update(float delta){
		if( delta == 0 || BludBourne._player.getState() == Entity.State.PAUSE){
			return;
		}
		
		processInput(delta);
		
		//if( !isCollisionWithNextPosition() ){
		//	BludBourne._player.setNextPositionToCurrent();
		//}
		
		//Gdx.app.debug(TAG, "update:: Next Position: (" + player.getNextPosition().x + "," + player.getNextPosition().y + ")" + "DELTA: " + delta );

	}
	
	public static void hide(){
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	}
	
	private void processInput(float delta){
		
		//Keyboard input
		if( keys.get(Keys.LEFT)){
			BludBourne._player.calculateNextPosition(Entity.Direction.LEFT, delta);
			BludBourne._player.setState(Entity.State.WALKING);
			BludBourne._player.setDirection(Entity.Direction.LEFT);
		}else if( keys.get(Keys.RIGHT)){
			BludBourne._player.calculateNextPosition(Entity.Direction.RIGHT, delta);
			BludBourne._player.setState(Entity.State.WALKING);
			BludBourne._player.setDirection(Entity.Direction.RIGHT);
		}else if( keys.get(Keys.UP)){
			BludBourne._player.calculateNextPosition(Entity.Direction.UP, delta);
			BludBourne._player.setState(Entity.State.WALKING);
			BludBourne._player.setDirection(Entity.Direction.UP);
		}else if(keys.get(Keys.DOWN)){
			BludBourne._player.calculateNextPosition(Entity.Direction.DOWN, delta);
			BludBourne._player.setState(Entity.State.WALKING);
			BludBourne._player.setDirection(Entity.Direction.DOWN);
		}else if(keys.get(Keys.QUIT)){
			Gdx.app.exit();
		}else{
			BludBourne._player.setState(Entity.State.IDLE);
		}
		
		//Mouse input
		if( mouseButtons.get(Mouse.SELECT)) {
			//Gdx.app.debug(TAG, "Mouse LEFT click at : (" + lastMouseCoordinates.x + "," + lastMouseCoordinates.y + ")" );
			mouseButtons.put(Mouse.SELECT, false);
		}

	}

	/*
	private boolean isCollisionWithNextPosition(){
		boolean isMapCollision = BludBourne._mainGameScreen.isCollisionWithMap(BludBourne._player);
		return isMapCollision;
	}
    */

}
