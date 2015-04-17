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
	if( BludBourne._AssetManager.isLoaded(assetFilenamePath) ){
		BludBourne._AssetManager.unload(assetFilenamePath);
		} else {
			Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
		}
	}

	public static float loadCompleted(){
		return BludBourne._AssetManager.getProgress();
	}

	public static int numberAssetsQueued(){
		return BludBourne._AssetManager.getQueuedAssets();
	}

   	public static boolean updateAssetLoading(){
		return BludBourne._AssetManager.update();
	}

	public static boolean isAssetLoaded(String fileName){
	   return BludBourne._AssetManager.isLoaded(fileName);

	}

	public static void loadMapAsset(String mapFilenamePath){
	   if( mapFilenamePath == null || mapFilenamePath.isEmpty() ){
		   return;
	   }

	   //load asset
		if( _filePathResolver.resolve(mapFilenamePath).exists() ){
			BludBourne._AssetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver));
			BludBourne._AssetManager.load(mapFilenamePath, TiledMap.class);
			//Until we add loading screen, just block until we load the map
			BludBourne._AssetManager.finishLoadingAsset(mapFilenamePath);
			Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
		}
		else{
			Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath );
		}
	}


	public static TiledMap getMapAsset(String mapFilenamePath){
		TiledMap map = null;

		// once the asset manager is done loading
		if( BludBourne._AssetManager.isLoaded(mapFilenamePath) ){
			map = BludBourne._AssetManager.get(mapFilenamePath,TiledMap.class);
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
			BludBourne._AssetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));
			BludBourne._AssetManager.load(textureFilenamePath, Texture.class);
			//Until we add loading screen, just block until we load the map
			BludBourne._AssetManager.finishLoadingAsset(textureFilenamePath);
		}
		else{
			Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
		}
	}

	public static Texture getTextureAsset(String textureFilenamePath){
		Texture texture = null;

		// once the asset manager is done loading
		if( BludBourne._AssetManager.isLoaded(textureFilenamePath) ){
			texture = BludBourne._AssetManager.get(textureFilenamePath,Texture.class);
		} else {
			Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
		}

		return texture;
	}


}
