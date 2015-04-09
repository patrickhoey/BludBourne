package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.PlayerController;
import com.packtpub.libgdx.bludbourne.Utility;


public class MainGameScreen implements Screen {
	private static class VIEWPORT {
		static float viewportWidth;
		static float viewportHeight;
		static float virtualWidth;
		static float virtualHeight;
		static float physicalWidth;
		static float physicalHeight;
		static float aspectRatio;
	}

	private PlayerController _controller;

	private static final String TAG = MainGameScreen.class.getSimpleName();

	private final float unitScale  = 1/16f;
	private String _overviewMap = "maps/world.tmx";
	private Sprite currentPlayerSprite;

	//private final static String MAP_BACKGROUND_LAYER = "MAP_BACKGROUND_LAYER";
	private OrthogonalTiledMapRenderer mapRenderer = null;
	private OrthographicCamera camera = null;
	private TiledMap currentMap = null;

	public MainGameScreen(){
	}

	@Override
	public void show() {
		//camera setup
		setupViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Utility.loadMapAsset(_overviewMap);
		if( Utility.isAssetLoaded(_overviewMap) ) {
			currentMap = Utility.getMapAsset(_overviewMap);
		}else{
			Gdx.app.debug(TAG, "Map not loaded" );
		}

		//get the current size
		camera = new OrthographicCamera(VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
		camera.setToOrtho(false,10*VIEWPORT.aspectRatio,10);
		//camera.setToOrtho(false, 20 * VIEWPORT.aspectRatio, 20);
		camera.update();

		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
		mapRenderer.setView(camera);


		currentPlayerSprite = BludBourne._player.getFrameSprite();

		_controller = new PlayerController();
		Gdx.input.setInputProcessor(_controller);
	}

	@Override
	public void hide() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_controller.update(delta);
		BludBourne._player.update(delta);

		mapRenderer.setView(camera);
		mapRenderer.render();

		mapRenderer.getBatch().begin();

		mapRenderer.getBatch().draw(currentPlayerSprite, currentPlayerSprite.getX(), currentPlayerSprite.getY(), 1,1);
		//mapRenderer.getBatch().draw(currentPlayerSprite, currentPlayerSprite.getX(), currentPlayerSprite.getY(), currentPlayerSprite.getOriginX(), currentPlayerSprite.getOriginY(),
		//		currentPlayerSprite.getWidth(), currentPlayerSprite.getHeight(), currentPlayerSprite.getScaleX(), currentPlayerSprite.getScaleY(), currentPlayerSprite.getRotation(),
		//		true);
		Gdx.app.debug(TAG, "currentPlayerSprite size: " + String.valueOf(currentPlayerSprite.getWidth()) + "," + String.valueOf(currentPlayerSprite.getHeight()) );
		mapRenderer.getBatch().end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		_controller.dispose();
		Gdx.input.setInputProcessor(null);
	}

	private void setupViewport(int width, int height){
		//Make the viewport a percentage of the total display area
		VIEWPORT.virtualWidth = width;
		VIEWPORT.virtualHeight = height;

		//Current viewport dimensions
		VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
		VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

		//pixel dimensions of display
		VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
		VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

		//aspect ratio for current viewport
		VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

		//update viewport if there could be skewing
		if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio){
			//Letterbox left and right
			VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
			VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
		}else{
			//letterbox above and below
			VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
			VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
		}

		Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
		Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
		Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
	}

	/*
	public boolean isCollisionWithMap(Rectangle boundingBox){

		if( currentMap == null ) return false;

		TiledMapTileLayer mapLayer =  (TiledMapTileLayer)currentMap.getLayers().get(currentMap.getMapCollisionLayer());

		String mapCollisionObjectLayer = currentMap.getMapCollisionObjectLayer();

		if( mapCollisionObjectLayer != null ){
			MapLayer collisionLayer =  (MapLayer)currentMap.getLayers().get(mapCollisionObjectLayer);
			boolean isCollision = isPixelCollisionWithMapLayer(boundingBox, mapLayer, collisionLayer);

			if( isCollision ){
				return true;
			}
		}

		String mapCollisionLayer = currentMap.getMapWallCollisionLayer();

		if( mapCollisionLayer != null ){
			TiledMapTileLayer mapWallLayer =  (TiledMapTileLayer)currentMap.getLayers().get(mapCollisionLayer);
			boolean isWallCollision = isCollisionWithMapLayer(boundingBox, mapWallLayer);

			if( isWallCollision ){
				return true;
			}
		}
		return false;
	}
	*/
}
