package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.ComponentObserver;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityConfig;
import com.packtpub.libgdx.bludbourne.InventoryItem;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.dialog.ConversationGraph;
import com.packtpub.libgdx.bludbourne.dialog.ConversationGraphObserver;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileObserver;

public class PlayerHUD implements Screen, ProfileObserver,ComponentObserver,ConversationGraphObserver,StoreInventoryObserver, StatusObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;
    private Entity _player;

    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private ConversationUI _conversationUI;
    private StoreInventoryUI _storeInventoryUI;

    private Json _json;
    private MapManager _mapMgr;

    public PlayerHUD(Camera camera, Entity player, MapManager mapMgr) {
        _camera = camera;
        _player = player;
        _mapMgr = mapMgr;
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);
        //_stage.setDebugAll(true);

        _json = new Json();

        _statusUI = new StatusUI();
        _statusUI.setVisible(true);
        _statusUI.setPosition(0, 0);

        _inventoryUI = new InventoryUI();
        _inventoryUI.setMovable(false);
        _inventoryUI.setVisible(false);
        _inventoryUI.setPosition(_stage.getWidth() / 2, 0);

        _conversationUI = new ConversationUI();
        _conversationUI.setMovable(true);
        _conversationUI.setVisible(false);
        _conversationUI.setPosition(_stage.getWidth() / 2, 0);
        _conversationUI.setWidth(_stage.getWidth() / 2);
        _conversationUI.setHeight(_stage.getHeight() / 2);

        _storeInventoryUI = new StoreInventoryUI();
        _storeInventoryUI.setMovable(false);
        _storeInventoryUI.setVisible(false);
        _storeInventoryUI.setPosition(0, 0);

        _stage.addActor(_storeInventoryUI);
        _stage.addActor(_inventoryUI);
        _stage.addActor(_conversationUI);
        _stage.addActor(_statusUI);

        //add tooltips to the stage
        Array<Actor> actors = _inventoryUI.getInventoryActors();
        for(Actor actor : actors){
            _stage.addActor(actor);
        }

        Array<Actor> storeActors = _storeInventoryUI.getInventoryActors();
        for(Actor actor : storeActors ){
            _stage.addActor(actor);
        }

        //Observers
        ProfileManager.getInstance().addObserver(this);
        _player.registerObserver(this);
        _statusUI.addObserver(this);
        _storeInventoryUI.addObserver(this);

        //Listeners
        ImageButton inventoryButton = _statusUI.getInventoryButton();
        inventoryButton.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                _inventoryUI.setVisible(_inventoryUI.isVisible()?false:true);
            }
        });

        _conversationUI.getCloseButton().addListener(new ClickListener() {
                                                         @Override
                                                         public void clicked(InputEvent event, float x, float y) {
                                                             _conversationUI.setVisible(false);
                                                             _mapMgr.clearCurrentSelectedMapEntity();
                                                         }
                                                     }
        );

        _storeInventoryUI.getCloseButton().addListener(new ClickListener() {
                                                         @Override
                                                         public void clicked(InputEvent event, float x, float y) {
                                                             _storeInventoryUI.setVisible(false);
                                                             _mapMgr.clearCurrentSelectedMapEntity();
                                                         }
                                                     }
        );
    }

    public Stage getStage() {
        return _stage;
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch(event){
            case PROFILE_LOADED:
                Array<InventoryItemLocation> inventory = profileManager.getProperty("playerInventory", Array.class);
                if( inventory != null && inventory.size > 0 ){
                    InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), inventory, _inventoryUI.getDragAndDrop());
                }else{
                    //add default items if nothing is found
                    Array<ItemTypeID> items = _player.getEntityConfig().getInventory();
                    Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                    for( int i = 0; i < items.size; i++){
                        itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                    }
                    InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), itemLocations, _inventoryUI.getDragAndDrop());
                }

                Array<InventoryItemLocation> equipInventory = profileManager.getProperty("playerEquipInventory", Array.class);
                if( equipInventory != null && equipInventory.size > 0 ){
                    InventoryUI.populateInventory(_inventoryUI.getEquipSlotTable(), equipInventory, _inventoryUI.getDragAndDrop());
                }

                //Check gold
                int goldVal = profileManager.getProperty("currentPlayerGP", Integer.class);
                if( goldVal < 0 ){
                    //start the player with some money
                    goldVal = 20;
                }
                _statusUI.setGoldValue(goldVal);

                break;
            case SAVING_PROFILE:
                profileManager.setProperty("playerInventory", InventoryUI.getInventory(_inventoryUI.getInventorySlotTable()));
                profileManager.setProperty("playerEquipInventory", InventoryUI.getInventory(_inventoryUI.getEquipSlotTable()));
                profileManager.setProperty("currentPlayerGP", _statusUI.getGoldValue() );
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(String value, ComponentEvent event) {
        switch(event) {
            case LOAD_CONVERSATION:
                EntityConfig config = _json.fromJson(EntityConfig.class, value);
                _conversationUI.loadConversation(config);
                _conversationUI.getCurrentConversationGraph().addObserver(this);
                break;
            case SHOW_CONVERSATION:
                EntityConfig configShow = _json.fromJson(EntityConfig.class, value);
                if( configShow.getEntityID().equalsIgnoreCase(_conversationUI.getCurrentEntityID())) {
                    _conversationUI.setVisible(true);
                }
                break;
            case HIDE_CONVERSATION:
                EntityConfig configHide = _json.fromJson(EntityConfig.class, value);
                if( configHide.getEntityID().equalsIgnoreCase(_conversationUI.getCurrentEntityID())) {
                    _conversationUI.setVisible(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(ConversationGraph graph, ConversationCommandEvent event) {
        switch(event) {
            case LOAD_STORE_INVENTORY:
                Entity selectedEntity = _mapMgr.getCurrentSelectedMapEntity();
                if( selectedEntity == null ){
                    break;
                }

                Array<InventoryItemLocation> inventory =  InventoryUI.getInventory(_inventoryUI.getInventorySlotTable());
                _storeInventoryUI.loadPlayerInventory(inventory);

                Array<InventoryItem.ItemTypeID> items  = selectedEntity.getEntityConfig().getInventory();
                Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                for( int i = 0; i < items.size; i++){
                    itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                }

                _storeInventoryUI.loadStoreInventory(itemLocations);

                _conversationUI.setVisible(false);
                _storeInventoryUI.toFront();
                _storeInventoryUI.setVisible(true);
                break;
            case EXIT_CONVERSATION:
                _conversationUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();
                break;
            case NONE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(String value, StoreInventoryEvent event) {
        switch (event) {
            case PLAYER_GP_TOTAL_UPDATED:
                int val = Integer.valueOf(value);
                _statusUI.setGoldValue(val);
                break;
            case PLAYER_INVENTORY_UPDATED:
                //_json.fromJson(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(int value, StatusEvent event) {
        switch(event) {
            case UPDATED_GP:
                _storeInventoryUI.setPlayerGP(value);
                break;
            default:
                break;
        }
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
