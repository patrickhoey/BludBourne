package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PlayerHUD implements Screen {

    public final static String STATUSUI_TEXTURE_ATLAS_PATH = "skins/statusui.atlas";

    private Stage _stage;
    private Viewport _viewport;
    private StatusUI _statusUI;

    public PlayerHUD(Camera camera) {
        _viewport = new ScreenViewport(camera);

        _stage = new Stage(_viewport);

        _statusUI = new StatusUI();
        _statusUI.setPosition(0, 0);

        _stage.addActor(_statusUI);
    }

    public Stage getStage() {
        return _stage;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        _stage.act(delta);
        _stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        _stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        _stage.dispose();
    }
}
