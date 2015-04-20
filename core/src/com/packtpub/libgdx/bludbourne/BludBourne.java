package com.packtpub.libgdx.bludbourne;

import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;
import com.badlogic.gdx.Game;


public class BludBourne extends Game {

	public static final MainGameScreen _mainGameScreen = new MainGameScreen();

	public static Entity _player;

	@Override
	public void create(){
		_player = new Entity();
		setScreen(_mainGameScreen);
	}

	@Override
	public void dispose(){
		_player.dispose();
		_mainGameScreen.dispose();
	}

}
