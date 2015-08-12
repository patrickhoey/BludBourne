package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
import com.packtpub.libgdx.bludbourne.battle.MonsterFactory;
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
    private Label _label;
    private boolean _isCameraFixed = true;
    private Image _transitionImage;
    private Action _screenFadeOutAction;
    private Action _screenFadeInAction;

    public CutSceneScreen(BludBourne game) {
        super(game);

        _json = new Json();

        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        _UIViewport = new ScreenViewport(_hudCamera);
        _UIStage = new Stage(_UIViewport);

        _label = new Label("Test", Utility.STATUSUI_SKIN);
        _label.setWrap(true);

        _messageBoxUI = new Dialog("", Utility.STATUSUI_SKIN, "solidbackground");
        _messageBoxUI.setVisible(false);
        _messageBoxUI.getContentTable().add(_label).width(_stage.getWidth()/2).pad(10, 10, 10, 0);
        _messageBoxUI.pack();
        _messageBoxUI.setPosition(_stage.getWidth() / 2 - _messageBoxUI.getWidth() / 2, _stage.getHeight() - _messageBoxUI.getHeight());

        _followingActor = new Actor();
        _followingActor.setPosition(0, 0);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        _transitionImage = new Image();
        _transitionImage.setFillParent(true);
        _transitionImage.setDrawable(drawable);
        _transitionImage.addAction(
                Actions.sequence(
                        Actions.alpha(0)));

        _screenFadeOutAction = new Action() {
            @Override
            public boolean act(float delta) {
                _transitionImage.addAction(
                        Actions.sequence(
                                Actions.alpha(0),
                                Actions.fadeIn(3)
                        ));
                return true;
            }
        };

        _screenFadeInAction = new Action() {
            @Override
            public boolean act(float delta) {
                _transitionImage.addAction(
                        Actions.sequence(
                                Actions.alpha(1),
                                Actions.fadeOut(3)
                        ));
                return true;
            }
        };


        Entity blackSmith = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.TOWN_BLACKSMITH);
        final AnimatedImage animBlackSmith = new AnimatedImage();
        animBlackSmith.setEntity(blackSmith);
        animBlackSmith.setPosition(10, 16);
        animBlackSmith.setSize(animBlackSmith.getWidth() * Map.UNIT_SCALE, animBlackSmith.getHeight() * Map.UNIT_SCALE);

        Entity innKeeper = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.TOWN_INNKEEPER);
        final AnimatedImage animInnKeeper = new AnimatedImage();
        animInnKeeper.setEntity(innKeeper);
        animInnKeeper.setPosition(12, 15);
        animInnKeeper.setSize(animInnKeeper.getWidth() * Map.UNIT_SCALE, animInnKeeper.getHeight() * Map.UNIT_SCALE);

        Entity mage = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.TOWN_MAGE);
        final AnimatedImage animMage = new AnimatedImage();
        animMage.setEntity(mage);
        animMage.setPosition(11, 17);
        animMage.setSize(animMage.getWidth() * Map.UNIT_SCALE, animMage.getHeight() * Map.UNIT_SCALE);

        Entity fire = EntityFactory.getInstance().getEntityByName(EntityFactory.EntityName.FIRE);
        final AnimatedImage animFire = new AnimatedImage();
        animFire.setEntity(fire);
        animFire.setSize(animFire.getWidth() * Map.UNIT_SCALE, animFire.getHeight() * Map.UNIT_SCALE);

        Entity demon = MonsterFactory.getInstance().getMonster(MonsterFactory.MonsterEntityType.MONSTER042);
        final AnimatedImage animDemon = new AnimatedImage();
        animDemon.setEntity(demon);
        animDemon.setSize(animDemon.getWidth() * Map.UNIT_SCALE, animDemon.getHeight() * Map.UNIT_SCALE);
        animDemon.setVisible(false);

        _stage.addAction(
                Actions.sequence(
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        _mapMgr.loadMap(MapFactory.MapType.TOWN);
                                        _mapMgr.disableCurrentmapMusic();
                                        setCameraPosition(10, 16);
                                        showMessage("BLACKSMITH: We have planned this long enough. The time is now! I have had enough talk...");
                                    }
                                }),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("MAGE: This is dark magic you fool. We must proceed with caution, or this could end badly for all of us");
                                    }
                                }),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("INNKEEPER: Both of you need to keep it down. If we get caught using black magic, we will all be hanged!");
                                    }
                                }),
                        Actions.delay(5),
                        Actions.addAction(_screenFadeOutAction),
                        Actions.delay(3),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        hideMessage();
                                        _mapMgr.loadMap(MapFactory.MapType.TOP_WORLD);
                                        _mapMgr.disableCurrentmapMusic();
                                        setCameraPosition(50, 30);
                                        animBlackSmith.setPosition(50, 30);
                                        animInnKeeper.setPosition(52, 30);
                                        animMage.setPosition(50, 28);
                                        animFire.setPosition(52, 28);
                                    }
                                }),
                        Actions.addAction(_screenFadeInAction),
                        Actions.delay(3),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("BLACKSMITH: Now, let's get on with this. I don't like the cemeteries very much...");
                                    }
                                }
                        ),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("MAGE: I told you, we can't rush the spell. Bringing someone back to life isn't simple!");
                                    }
                                }
                        ),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("INNKEEPER: I know you loved your daughter, but this just isn't right...");
                                    }
                                }
                        ),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("BLACKSMITH: You have never had a child of your own. You just don't understand!");
                                    }
                                }
                        ),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("MAGE: You both need to concentrate, wait...Oh no, something is wrong!!");
                                    }
                                }
                        ),
                        Actions.delay(7),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        hideMessage();
                                        animDemon.setPosition(52, 28);
                                        animDemon.setVisible(true);
                                    }
                                }
                        ),
                        Actions.addAction(Actions.fadeOut(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.fadeIn(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.fadeOut(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.fadeIn(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.fadeOut(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.fadeIn(2), animDemon),
                        Actions.delay(2),
                        Actions.addAction(Actions.scaleBy(20, 20, 5, Interpolation.bounce), animDemon),
                        Actions.delay(5),
                        Actions.addAction(Actions.moveBy(20, 0), animDemon),
                        Actions.delay(2),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("BLACKSMITH: What...What have we done...");
                                    }
                                }
                        ),
                        Actions.delay(3),
                        Actions.addAction(_screenFadeOutAction),
                        Actions.delay(3),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        hideMessage();
                                        animBlackSmith.setVisible(false);
                                        animInnKeeper.setVisible(false);
                                        animMage.setVisible(false);
                                        animFire.setVisible(false);

                                        _mapMgr.loadMap(MapFactory.MapType.TOP_WORLD);
                                        _mapMgr.disableCurrentmapMusic();

                                        animDemon.setVisible(true);
                                        animDemon.setScale(1, 1);
                                        animDemon.setSize(16 * Map.UNIT_SCALE, 16 * Map.UNIT_SCALE);
                                        animDemon.setPosition(50, 40);

                                        followActor(animDemon);
                                    }
                                }),
                        Actions.addAction(_screenFadeInAction),
                        Actions.addAction(Actions.moveTo(54, 65, 13, Interpolation.linear), animDemon),
                        Actions.delay(10),
                        Actions.addAction(_screenFadeOutAction),
                        Actions.delay(3),
                        Actions.addAction(_screenFadeInAction),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        hideMessage();
                                        animBlackSmith.setVisible(false);
                                        animInnKeeper.setVisible(false);
                                        animMage.setVisible(false);
                                        animFire.setVisible(false);
                                        _mapMgr.loadMap(MapFactory.MapType.CASTLE_OF_DOOM);
                                        _mapMgr.disableCurrentmapMusic();
                                        followActor(animDemon);
                                        animDemon.setVisible(true);
                                        animDemon.setPosition(15, 1);
                                    }
                                }),
                        Actions.addAction(Actions.moveTo(15, 76, 15, Interpolation.linear), animDemon),
                        Actions.delay(15),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("DEMON: I will now send my legions of demons to destroy these sacks of meat!");
                                    }
                                }
                        ),
                        Actions.delay(5),
                        Actions.addAction(_screenFadeOutAction)

                )
        );

        _stage.addActor(animFire);
        _stage.addActor(animDemon);
        _stage.addActor(animMage);
        _stage.addActor(animBlackSmith);
        _stage.addActor(animInnKeeper);
        _stage.addActor(_transitionImage);
        _transitionImage.toFront();

        _UIStage.addActor(_messageBoxUI);
    }

    public void followActor(Actor actor){
        _followingActor = actor;
        _isCameraFixed = false;
    }

    public void setCameraPosition(float x, float y){
        _camera.position.set(x, y, 0f);
        _isCameraFixed = true;
    }

    public void showMessage(String message){
        _label.setText(message);
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

        //_mapMgr.updateCurrentMapEntities(_mapMgr, _mapRenderer.getBatch(), delta);

        if( !_isCameraFixed ){
            _camera.position.set(_followingActor.getX(), _followingActor.getY(), 0f);
            _camera.update();
        }

        _UIStage.act(delta);
        _UIStage.draw();

        _stage.act(delta);
        _stage.draw();
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
