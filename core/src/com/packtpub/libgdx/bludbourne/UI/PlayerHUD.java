package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;


public class PlayerHUD implements Screen {

    private final static String STATUSUI_TEXTURE_ATLAS_PATH = "skins/statusui.atlas";
    private final static String STATUSUI_SKIN_PATH = "skins/statusui.json";
    private final static String ITEMS_TEXTURE_ATLAS_PATH = "skins/items.atlas";
    private final static String ITEMS_SKIN_PATH = "skins/items.json";

    private Stage _stage;
    private Viewport _viewport;
    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private Camera _camera;

    public static TextureAtlas statusUITextureAtlas = new TextureAtlas(STATUSUI_TEXTURE_ATLAS_PATH);
    public static TextureAtlas itemsTextureAtlas = new TextureAtlas(ITEMS_TEXTURE_ATLAS_PATH);
    public static Skin statusUISkin = new Skin(Gdx.files.internal(STATUSUI_SKIN_PATH), statusUITextureAtlas);

    public PlayerHUD(Camera camera) {
        _camera = camera;
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);
        //_stage.setDebugAll(true);

        _statusUI = new StatusUI(statusUISkin, statusUITextureAtlas);
        _statusUI.setPosition(0, 0);

        _inventoryUI = new InventoryUI(statusUISkin, statusUITextureAtlas);
        _inventoryUI.setMovable(false);

        float centerX = (_stage.getWidth() - _inventoryUI.getWidth())/2;
        float centerY = (_stage.getHeight() - _inventoryUI.getHeight()) / 2;
        _inventoryUI.setPosition(centerX, centerY);

        //_stage.addActor(_statusUI);
        _stage.addActor(_inventoryUI);

        //add tooltips to the stage
        Array<Actor> actors = _inventoryUI.getInventoryActors();
        for(Actor actor : actors){
            _stage.addActor(actor);
        }
    }

    public void populateInventory(Array<ItemTypeID> itemTypeIDs){
        _inventoryUI.populateInventory(itemTypeIDs);
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
