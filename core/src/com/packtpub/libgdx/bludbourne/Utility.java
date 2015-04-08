package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public final class Utility {
	private static final String TAG = Utility.class.getSimpleName();

	public static void unloadAsset(String assetFilenamePath){
	// once the asset manager is done loading
	if( BludBourne._AssetManager.isLoaded(assetFilenamePath) ){
		BludBourne._AssetManager.unload(assetFilenamePath);
		} else {
			//Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
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
		InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();
		if( filePathResolver.resolve(mapFilenamePath).exists() ){
			BludBourne._AssetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
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
}
