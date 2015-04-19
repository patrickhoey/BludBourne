package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.PlayerController;

public class MainGameScreen implements Screen {
	private static final String TAG = MainGameScreen.class.getSimpleName();

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
	private TextureRegion _currentPlayerFrame;
	private Sprite _currentPlayerSprite;

	private OrthogonalTiledMapRenderer _mapRenderer = null;
	private OrthographicCamera _camera = null;
	private static MapManager _mapMgr;

	public MainGameScreen(){
		_mapMgr = new MapManager();
	}

	@Override
	public void show() {
		//_camera setup
		setupViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//get the current size
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 10 * VIEWPORT.aspectRatio, 10);

		_mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
		_mapRenderer.setView(_camera);

		Gdx.app.debug(TAG, "UnitScale value is: " + _mapRenderer.getUnitScale());

		_currentPlayerSprite = BludBourne._player.getFrameSprite();

		_controller = new PlayerController();
		Gdx.input.setInputProcessor(_controller);

		BludBourne._player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);
	}

	@Override
	public void hide() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Preferable to lock and center the _camera to the player's position
		_camera.position.set(_currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 0f);
		_camera.update();

		BludBourne._player.update(delta);
		_currentPlayerFrame = BludBourne._player.getFrame();

		updatePortalLayerActivation(BludBourne._player.boundingBox);

		if( !isCollisionWithMapLayer(BludBourne._player.boundingBox) ){
			BludBourne._player.setNextPositionToCurrent();
		}
		_controller.update(delta);

		//_mapRenderer.getBatch().enableBlending();
		//_mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		_mapRenderer.setView(_camera);
		_mapRenderer.render();

		_mapRenderer.getBatch().begin();
		_mapRenderer.getBatch().draw(_currentPlayerFrame, _currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 1,1);
		_mapRenderer.getBatch().end();
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
		_mapRenderer.dispose();
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

	private boolean isCollisionWithMapLayer(Rectangle boundingBox){
		MapLayer mapCollisionLayer =  _mapMgr.getCollisionLayer();

		if( mapCollisionLayer == null ){
			return false;
		}

		Rectangle rectangle = null;

		for( MapObject object: mapCollisionLayer.getObjects()){
			if(object instanceof RectangleMapObject) {
				rectangle = ((RectangleMapObject)object).getRectangle();
				//Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
				//Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
				if( boundingBox.overlaps(rectangle) ){
					//Gdx.app.debug(TAG, "Map Collision!");
					return true;
				}
			}
		}

		return false;
	}

	private boolean updatePortalLayerActivation(Rectangle boundingBox){
		MapLayer mapPortalLayer =  _mapMgr.getPortalLayer();

		if( mapPortalLayer == null ){
			return false;
		}

		Rectangle rectangle = null;

		for( MapObject object: mapPortalLayer.getObjects()){
			if(object instanceof RectangleMapObject) {
				rectangle = ((RectangleMapObject)object).getRectangle();
				//Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
				//Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
				if( boundingBox.overlaps(rectangle) ){
					String mapName = object.getName();
					if( mapName == null ) {
						return false;
					}

					_mapMgr.setClosestStartPositionFromScaledUnits(BludBourne._player.getCurrentPosition());
					_mapMgr.loadMap(mapName);
					BludBourne._player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);
					_mapRenderer.setMap(_mapMgr.getCurrentMap());
					Gdx.app.debug(TAG, "Portal Activated");
					return true;
				}
			}
		}

		return false;
	}


}
