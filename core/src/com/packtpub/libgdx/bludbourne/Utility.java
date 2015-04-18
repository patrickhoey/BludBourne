package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public final class Utility {
	private static final String TAG = Utility.class.getSimpleName();

	private static InternalFileHandleResolver _filePathResolver =  new InternalFileHandleResolver();

	public static void unloadAsset(String assetFilenamePath){
	// once the asset manager is done loading
	if( BludBourne._assetManager.isLoaded(assetFilenamePath) ){
		BludBourne._assetManager.unload(assetFilenamePath);
		} else {
			Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
		}
	}

	public static float loadCompleted(){
		return BludBourne._assetManager.getProgress();
	}

	public static int numberAssetsQueued(){
		return BludBourne._assetManager.getQueuedAssets();
	}

   	public static boolean updateAssetLoading(){
		return BludBourne._assetManager.update();
	}

	public static boolean isAssetLoaded(String fileName){
	   return BludBourne._assetManager.isLoaded(fileName);

	}

	public static void loadMapAsset(String mapFilenamePath){
	   if( mapFilenamePath == null || mapFilenamePath.isEmpty() ){
		   return;
	   }

	   //load asset
		if( _filePathResolver.resolve(mapFilenamePath).exists() ){
			BludBourne._assetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver));
			BludBourne._assetManager.load(mapFilenamePath, TiledMap.class);
			//Until we add loading screen, just block until we load the map
			BludBourne._assetManager.finishLoadingAsset(mapFilenamePath);
			Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
		}
		else{
			Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath );
		}
	}


	public static TiledMap getMapAsset(String mapFilenamePath){
		TiledMap map = null;

		// once the asset manager is done loading
		if( BludBourne._assetManager.isLoaded(mapFilenamePath) ){
			map = BludBourne._assetManager.get(mapFilenamePath,TiledMap.class);
		} else {
			Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath );
		}

		return map;
	}

	public static void loadTextureAsset(String textureFilenamePath){
		if( textureFilenamePath == null || textureFilenamePath.isEmpty() ){
			return;
		}
		//load asset
		if( _filePathResolver.resolve(textureFilenamePath).exists() ){
			BludBourne._assetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));
			BludBourne._assetManager.load(textureFilenamePath, Texture.class);
			//Until we add loading screen, just block until we load the map
			BludBourne._assetManager.finishLoadingAsset(textureFilenamePath);
		}
		else{
			Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
		}
	}

	public static Texture getTextureAsset(String textureFilenamePath){
		Texture texture = null;

		// once the asset manager is done loading
		if( BludBourne._assetManager.isLoaded(textureFilenamePath) ){
			texture = BludBourne._assetManager.get(textureFilenamePath,Texture.class);
		} else {
			Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
		}

		return texture;
	}


}
