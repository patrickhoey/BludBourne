package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityFactory;
import com.packtpub.libgdx.bludbourne.Map;
import com.packtpub.libgdx.bludbourne.MapFactory;
import com.packtpub.libgdx.bludbourne.UI.AnimatedImage;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

public class CutSceneScreen extends MainGameScreen {
    private Json _json;
    private Stage _stage;
    private Viewport _viewport;
    private Stage _UIStage;
    private Viewport _UIViewport;
    private AnimatedImage _animImage;
    private Entity _entity;
    private Actor _followingActor;
    private Dialog _messageBoxUI;

    public CutSceneScreen(BludBourne game) {
        super(game);

        _json = new Json();

        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        _UIViewport = new ScreenViewport(_hudCamera);
        _UIStage = new Stage(_UIViewport);

        _messageBoxUI = new Dialog("", Utility.STATUSUI_SKIN, "solidbackground"){
            {
                text("");
            }
            @Override
            protected void result(final Object object){
                cancel();
                setVisible(false);
            }

        };
        _messageBoxUI.setVisible(false);
        _messageBoxUI.pack();
        _messageBoxUI.setPosition(_stage.getWidth() / 2 - _messageBoxUI.getWidth() / 2, _stage.getHeight() / 2 - _messageBoxUI.getHeight() / 2);

        _followingActor = new Actor();
        _followingActor.setPosition(0,0);

        _entity = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.PLAYER_PUPPET);

        _animImage = new AnimatedImage();
        _animImage.setEntity(_entity);
        _animImage.setPosition(1, 1);
        _animImage.setSize(_animImage.getWidth() * Map.UNIT_SCALE, _animImage.getHeight() * Map.UNIT_SCALE);

        _animImage.addAction(
                Actions.sequence(
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        followActor(_animImage);
                                    }
                                }),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        _animImage.setCurrentAnimation(Entity.AnimationType.WALK_RIGHT);
                                        float width = _animImage.getWidth() * Map.UNIT_SCALE;
                                        float height = _animImage.getHeight() * Map.UNIT_SCALE;
                                        _animImage.setSize(width, height);
                                    }
                                }),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("We begin our adventure...");
                                    }
                                }),
                        Actions.delay(3),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        hideMessage();
                                    }
                                }),
                        Actions.moveTo(10, 1, 10f),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        _animImage.setCurrentAnimation(Entity.AnimationType.WALK_UP);
                                        float width = _animImage.getWidth() * Map.UNIT_SCALE;
                                        float height = _animImage.getHeight() * Map.UNIT_SCALE;
                                        _animImage.setSize(width, height);
                                    }
                                }),
                        Actions.moveTo(10, 10, 10f),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        _mapMgr.loadMap(MapFactory.MapType.CASTLE_OF_DOOM);
                                    }
                                })

                )
        );

        _stage.addActor(_animImage);

        _UIStage.addActor(_messageBoxUI);
    }

    public void followActor(Actor actor){
        _followingActor = actor;
    }

    public void showMessage(String message){
        _messageBoxUI.text(message);
        _messageBoxUI.pack();
        _messageBoxUI.setVisible(true);
    }

    public void hideMessage(){
        _messageBoxUI.setVisible(false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _mapRenderer.setView(_camera);

        _mapRenderer.getBatch().enableBlending();
        _mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if( _mapMgr.hasMapChanged() ){
            _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());
            _mapMgr.setMapChanged(false);
        }

        _mapRenderer.render();

        _camera.position.set(_followingActor.getX(), _followingActor.getY(), 0f);
        _camera.update();

        _stage.act(delta);
        _stage.draw();

        _UIStage.act(delta);
        _UIStage.draw();
    }

    @Override
    public void show() {
        ProfileManager.getInstance().removeAllObservers();
        if( _mapRenderer == null ){
            _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        }
    }

    @Override
    public void hide() {
        ProfileManager.getInstance().removeAllObservers();
        Gdx.input.setInputProcessor(null);
    }


}
