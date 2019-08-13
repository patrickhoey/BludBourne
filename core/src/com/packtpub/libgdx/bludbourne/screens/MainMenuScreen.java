package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.packtpub.libgdx.bludbourne.BludBourne.ScreenType;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;

public class MainMenuScreen extends GameScreen {

	private Stage _stage;
	private BludBourne _game;

	public MainMenuScreen(BludBourne game){
		_game = game;

		//creation
		_stage = new Stage();
		Table table = new Table();
		table.setFillParent(true);

		Image title = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("bludbourne_title"));
		TextButton newGameButton = new TextButton("New Game", Utility.STATUSUI_SKIN);
		TextButton loadGameButton = new TextButton("Load Game", Utility.STATUSUI_SKIN);
		TextButton watchIntroButton = new TextButton("Watch Intro", Utility.STATUSUI_SKIN);
		TextButton creditsButton = new TextButton("Credits", Utility.STATUSUI_SKIN);
		TextButton exitButton = new TextButton("Exit",Utility.STATUSUI_SKIN);


		//Layout
		table.add(title).spaceBottom(75).row();
		table.add(newGameButton).spaceBottom(10).row();
		table.add(loadGameButton).spaceBottom(10).row();
		table.add(watchIntroButton).spaceBottom(10).row();
		table.add(creditsButton).spaceBottom(10).row();
		table.add(exitButton).spaceBottom(10).row();

		_stage.addActor(table);

		//Listeners
		newGameButton.addListener(new ClickListener() {
									  @Override
									  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
										  return true;
									  }

									  @Override
									  public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
										  _game.setScreen(_game.getScreenType(ScreenType.NewGame));
									  }
								  }
		);

		loadGameButton.addListener(new ClickListener() {

									   @Override
									   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
										   return true;
									   }

									   @Override
									   public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
										   _game.setScreen(_game.getScreenType(ScreenType.LoadGame));
									   }
								   }
		);

		exitButton.addListener(new ClickListener() {

								   @Override
								   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
									   return true;
								   }

								   @Override
								   public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
									   Gdx.app.exit();
								   }

							   }
		);

		watchIntroButton.addListener(new ClickListener() {

										 @Override
										 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
											 return true;
										 }

										 @Override
										 public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
											 MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
											 _game.setScreen(_game.getScreenType(ScreenType.WatchIntro));
										 }
									 }
		);

		creditsButton.addListener(new ClickListener() {

										 @Override
										 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
											 return true;
										 }

										 @Override
										 public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
											 _game.setScreen(_game.getScreenType(ScreenType.Credits));
										 }
									 }
		);

		notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_stage.act(delta);
		_stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		_stage.getViewport().setScreenSize(width, height);
	}

	@Override
	public void show() {
		notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
		Gdx.input.setInputProcessor(_stage);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		_stage.dispose();
	}

}



