package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;
import com.badlogic.gdx.Game;


public class BludBourne extends Game {

	public static final MainGameScreen mainGameScreen = new MainGameScreen();
	public static final AssetManager _AssetManager = new AssetManager();

	@Override
	public void create(){
		setScreen(mainGameScreen);
	}

}
