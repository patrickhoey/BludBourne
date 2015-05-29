package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.packtpub.libgdx.bludbourne.BludBourne.ScreenType;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

public class NewGameScreen implements Screen {

	private Stage _stage;
	private BludBourne _game;

	public NewGameScreen(BludBourne game){
		_game = game;

		//create
		_stage = new Stage();

		Label profileName = new Label("Enter Profile Name: ", Utility.STATUSUI_SKIN);
		final TextField profileText = new TextField("",Utility.STATUSUI_SKIN, "inventory");
		profileText.setMaxLength(20);

		final Dialog overwriteDialog = new Dialog("Overwrite?", Utility.STATUSUI_SKIN, "solidbackground");
		Label overwriteLabel = new Label("Overwrite existing profile name?", Utility.STATUSUI_SKIN);
		TextButton cancelButton = new TextButton("Cancel", Utility.STATUSUI_SKIN, "inventory");

		TextButton overwriteButton = new TextButton("Overwrite", Utility.STATUSUI_SKIN, "inventory");
		overwriteDialog.setKeepWithinStage(true);
		overwriteDialog.setModal(true);
		overwriteDialog.setMovable(false);
		overwriteDialog.text(overwriteLabel);

		TextButton startButton = new TextButton("Start", Utility.STATUSUI_SKIN);
		TextButton backButton = new TextButton("Back", Utility.STATUSUI_SKIN);

		//Layout
		overwriteDialog.row();
		overwriteDialog.button(overwriteButton).bottom().left();
		overwriteDialog.button(cancelButton).bottom().right();

		Table topTable = new Table();
		topTable.setFillParent(true);
		topTable.add(profileName).center();
		topTable.add(profileText).center();

		Table bottomTable = new Table();
		bottomTable.setHeight(startButton.getHeight());
		bottomTable.setWidth(Gdx.graphics.getWidth());
		bottomTable.center();
		bottomTable.add(startButton).padRight(50);
		bottomTable.add(backButton);

		_stage.addActor(topTable);
		_stage.addActor(bottomTable);

		//Listeners
		cancelButton.addListener(new InputListener() {

									 @Override
									 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button ){
										 overwriteDialog.hide();
										 return true;
									 }
								 }
		);

		overwriteButton.addListener(new InputListener() {

										@Override
										public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
											String messageText = profileText.getText();
											ProfileManager.getInstance().writeProfileToStorage(messageText, "", true);
											ProfileManager.getInstance().setCurrentProfile(messageText);
											ProfileManager.getInstance().saveProfile();
											ProfileManager.getInstance().loadProfile();
											_game.setScreen(_game.getScreenType(ScreenType.MainGame));
											return true;
										}
									}
		);

		startButton.addListener(new InputListener() {

									@Override
									public boolean touchDown(InputEvent event, float x, float y, int pointer, int button ){
										String messageText = profileText.getText();
										//check to see if the current profile matches one that already exists
										boolean exists = false;

										exists = ProfileManager.getInstance().doesProfileExist(messageText);

										if( exists ){
											//Pop up dialog for Overwrite
											overwriteDialog.show(_stage);
										}else{
											ProfileManager.getInstance().writeProfileToStorage(messageText,"",false);
											ProfileManager.getInstance().setCurrentProfile(messageText);
											ProfileManager.getInstance().saveProfile();
											ProfileManager.getInstance().loadProfile();
											_game.setScreen(_game.getScreenType(ScreenType.MainGame));
										}

										return true;
									}
								}
		);

		backButton.addListener(new InputListener() {

								   @Override
								   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
									   _game.setScreen(_game.getScreenType(ScreenType.MainMenu));
									   return true;
								   }
							   }
		);

	}

	@Override
	public void render(float delta) {
		if( delta == 0){
			return;
		}
		
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
		_stage.clear();
		_stage.dispose();
	}
	


}
