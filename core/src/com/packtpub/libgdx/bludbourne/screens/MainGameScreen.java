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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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

	private String _overviewMap = "maps/topworld.tmx";
	private String _townMap = "maps/town.tmx";
	private String _castleDoom = "maps/castle_of_doom.tmx";

	private String _defaultMap = _castleDoom;
	private TextureRegion currentPlayerFrame;
	private Sprite currentPlayerSprite;

	//private final static String MAP_BACKGROUND_LAYER = "MAP_BACKGROUND_LAYER";
	private final static String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
	private OrthogonalTiledMapRenderer mapRenderer = null;
	private OrthographicCamera camera = null;
	private TiledMap currentMap = null;

	public MainGameScreen(){
	}

	@Override
	public void show() {
		//camera setup
		setupViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Utility.loadMapAsset(_defaultMap);
		if( Utility.isAssetLoaded(_defaultMap) ) {
			currentMap = Utility.getMapAsset(_defaultMap);
		}else{
			Gdx.app.debug(TAG, "Map not loaded" );
		}

		//get the current size
		camera = new OrthographicCamera(VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
		camera.setToOrtho(false, 10 * VIEWPORT.aspectRatio, 10);

		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
		mapRenderer.setView(camera);
		Gdx.app.debug(TAG, "UnitScale value is: " + mapRenderer.getUnitScale());

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

		//Preferable to lock and center the camera to the player's position
		camera.position.set(currentPlayerSprite.getX(), currentPlayerSprite.getY(), 0f);
		camera.update();

		BludBourne._player.update(delta);
		currentPlayerFrame = BludBourne._player.getFrame();

		if( !isCollisionWithMap(BludBourne._player.boundingBox) ){
			BludBourne._player.setNextPositionToCurrent();
		}
		_controller.update(delta);

		//mapRenderer.getBatch().enableBlending();
		//mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		mapRenderer.setView(camera);
		mapRenderer.render();

		mapRenderer.getBatch().begin();
		mapRenderer.getBatch().draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY(), 1,1);
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


	public boolean isCollisionWithMap(Rectangle boundingBox){

		if( currentMap == null ) return false;

		MapLayer mapCollisionLayer =  currentMap.getLayers().get(MAP_COLLISION_LAYER);

		if( mapCollisionLayer != null ) {
			if (isCollisionWithMapLayer(boundingBox, mapCollisionLayer)) {
				return true;
			} else {
				return false;
			}
		}
		else{
			return false;
		}
	}

	private boolean isCollisionWithMapLayer(Rectangle boundingBox, MapLayer collisionLayer){
		if( collisionLayer == null ){
			return false;
		}

		//Gdx.app.debug(TAG, "Checking collision layer...");
		//Need to account for the unitscale, since the map coordinates will be in pixels
		if( unitScale > 0 )
			boundingBox.setPosition(boundingBox.x/unitScale, boundingBox.y/unitScale);

		Rectangle rectangle = null;

		for( MapObject object: collisionLayer.getObjects()){
			if(object instanceof RectangleMapObject) {
				rectangle = ((RectangleMapObject)object).getRectangle();
				//Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
				//Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
				if( boundingBox.overlaps(rectangle) ){
					return true;
				}
			}
		}

		return false;
	}


}
