package com.packtpub.libgdx.bludbourne;

import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;
import com.badlogic.gdx.Game;


public class BludBourne extends Game {

	private MainGameScreen _mainGameScreen;

	@Override
	public void create(){
		_mainGameScreen = new MainGameScreen();
		setScreen(_mainGameScreen);
	}

	@Override
	public void dispose(){
		_mainGameScreen.dispose();
	}

}
