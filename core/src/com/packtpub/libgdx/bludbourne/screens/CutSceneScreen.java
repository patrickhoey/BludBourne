package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Component;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityFactory;
import com.packtpub.libgdx.bludbourne.Map;
import com.packtpub.libgdx.bludbourne.UI.AnimatedImage;

public class CutSceneScreen extends MainGameScreen {
    private Json _json;
    private Stage _stage;
    private Viewport _viewport;
    private Image _image;
    private AnimatedImage _animImage;
    private Entity _entity;

    public CutSceneScreen(BludBourne game) {
        super(game);

        _json = new Json();

        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        _entity = EntityFactory.getEntity(EntityFactory.EntityType.NPC);
        _entity.setEntityConfig(_entity.loadEntityConfigByPath(EntityFactory.PLAYER_CONFIG));
        _entity.sendMessage(Component.MESSAGE.INIT_STATE, _json.toJson(Entity.State.WALKING)) ;
        _entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, _json.toJson(_entity.getEntityConfig()));

        _image = new Image(_entity.getAnimation(Entity.AnimationType.WALK_RIGHT).getKeyFrame(0));
        _image.setPosition(0, 0);
        _image.setSize(_image.getWidth() * Map.UNIT_SCALE, _image.getHeight() * Map.UNIT_SCALE);

        Animation animation = _entity.getAnimation(Entity.AnimationType.WALK_RIGHT);
        _animImage = new AnimatedImage();
        _animImage.setAnimation(animation);
        _animImage.setPosition(1, 1);
        _animImage.setSize(_animImage.getWidth() * Map.UNIT_SCALE, _animImage.getHeight() * Map.UNIT_SCALE);
        //_animImage.setDebug(true);

        _animImage.addAction(
                Actions.sequence(
                        Actions.run(
                                new Runnable(){
                                    @Override
                                    public void run() {
                                        _animImage.setAnimation(_entity.getAnimation(Entity.AnimationType.WALK_RIGHT));
                                        float width = _animImage.getWidth() * Map.UNIT_SCALE;
                                        float height =  _animImage.getHeight() * Map.UNIT_SCALE;
                                        _animImage.setSize(width, height);
                                    }}),
                        Actions.moveTo(10, 1, 10f),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        _animImage.setAnimation(_entity.getAnimation(Entity.AnimationType.WALK_UP));
                                        float width = _animImage.getWidth() * Map.UNIT_SCALE;
                                        float height =  _animImage.getHeight() * Map.UNIT_SCALE;
                                        _animImage.setSize(width, height);
                                    }
                                }),
                        Actions.moveTo(10, 10, 10f))
        );

        _stage.addActor(_animImage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _mapRenderer.setView(_camera);

        if( _mapMgr.hasMapChanged() ){
            _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());
            _mapMgr.setMapChanged(false);
        }

        _mapRenderer.render();

        _camera.position.set(_animImage.getX(), _animImage.getY(), 0f);
        _camera.update();

        //_player.updateInput(delta);
        //_player.updatePhysics(_mapMgr, delta);

        _stage.act(delta);
        _stage.draw();
    }

    @Override
    public void show() {
        if( _mapRenderer == null ){
            _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


}
