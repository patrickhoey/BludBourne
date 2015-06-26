package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Utility;

public class StoreInventoryUI extends Window implements InventorySlotObserver{

    private int _numStoreInventorySlots = 30;
    private int _lengthSlotRow = 10;
    private Table _inventorySlotTable;
    private Table _playerInventorySlotTable;
    private DragAndDrop _dragAndDrop;
    private Array<Actor> _inventoryActors;

    private static String STORE_INVENTORY = "Store_Inventory";
    private static String PLAYER_INVENTORY = "Player_Inventory";

    private final int _slotWidth = 52;
    private final int _slotHeight = 52;

    private InventorySlotTooltip _inventorySlotTooltip;

    private Label _sellTotalLabel;
    private Label _buyTotalLabel;

    private int _tradeInVal = 0;
    private int _fullValue = 0;

    private Button _sellButton;
    private Button _buyButton;

    private Table _buttons;
    private Table _totalLabels;

    private static String SELL = "SELL";
    private static String BUY = "BUY";
    private static String GP = " GP";

    public StoreInventoryUI(){
        super("Store Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        this.setFillParent(true);

        //create
        _dragAndDrop = new DragAndDrop();
        _inventoryActors = new Array<Actor>();
        _inventorySlotTable = new Table();
        _inventorySlotTable.setName(STORE_INVENTORY);

        _playerInventorySlotTable = new Table();
        _playerInventorySlotTable.setName(PLAYER_INVENTORY);
        _inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUSUI_SKIN);

        _sellButton = new TextButton(SELL, Utility.STATUSUI_SKIN, "inventory");
        _sellButton.setDisabled(true);
        _sellButton.setTouchable(Touchable.disabled);

        _sellTotalLabel = new Label(SELL + " : " + _tradeInVal + GP, Utility.STATUSUI_SKIN);
        _sellTotalLabel.setAlignment(Align.center);
        _buyTotalLabel = new Label(BUY + " : " + _fullValue + GP, Utility.STATUSUI_SKIN);
        _buyTotalLabel.setAlignment(Align.center);

        _buyButton = new TextButton(BUY, Utility.STATUSUI_SKIN, "inventory");
        _buyButton.setDisabled(true);
        _buyButton.setTouchable(Touchable.disabled);

        _buttons = new Table();
        _buttons.defaults().expand().fill();
        _buttons.add(_sellButton).padLeft(10).padRight(10);
        _buttons.add(_buyButton).padLeft(10).padRight(10);

        _totalLabels = new Table();
        _totalLabels.defaults().expand().fill();
        _totalLabels.add(_sellTotalLabel).padLeft(40);
        _totalLabels.add();
        _totalLabels.add(_buyTotalLabel).padRight(40);

        //layout
        for(int i = 1; i <= _numStoreInventorySlots; i++){
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
            inventorySlot.addObserver(this);
            inventorySlot.setName(STORE_INVENTORY);

            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            _inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            if(i % _lengthSlotRow == 0){
                _inventorySlotTable.row();
            }
        }

        for(int i = 1; i <= InventoryUI._numSlots; i++){
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
            inventorySlot.addObserver(this);
            inventorySlot.setName(PLAYER_INVENTORY);

            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            _playerInventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            if(i % _lengthSlotRow == 0){
                _playerInventorySlotTable.row();
            }
        }

        _inventoryActors.add(_inventorySlotTooltip);

        //this.debugAll();
        this.defaults().expand().fill();
        this.add(_inventorySlotTable).pad(10, 10, 10, 10);
        this.row();
        this.add(_buttons);
        this.row();
        this.add(_totalLabels);
        this.row();
        this.add(_playerInventorySlotTable).pad(10, 10, 10, 10);
        this.pack();
    }

    public Table getInventorySlotTable() {
        return _inventorySlotTable;
    }

    public Array<Actor> getInventoryActors(){
        return _inventoryActors;
    }

    public void loadPlayerInventory(Array<InventoryItemLocation> playerInventoryItems){
        InventoryUI.populateInventory(_playerInventorySlotTable, playerInventoryItems, _dragAndDrop);
    }

    public void loadStoreInventory(Array<InventoryItemLocation> storeInventoryItems){
        InventoryUI.populateInventory(_inventorySlotTable, storeInventoryItems, _dragAndDrop);
    }

    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {
        switch(event)
        {
            case ADDED_ITEM:
                if( slot.getTopInventoryItem().getName().equalsIgnoreCase(PLAYER_INVENTORY) &&
                        slot.getName().equalsIgnoreCase(STORE_INVENTORY) ) {
                    _tradeInVal += slot.getTopInventoryItem().getTradeValue();
                    _sellTotalLabel.setText(SELL + " : " + _tradeInVal + GP);
                    if( _tradeInVal > 0 ) {
                        _sellButton.setDisabled(false);
                        _sellButton.setTouchable(Touchable.enabled);
                    }
                }

                if( slot.getTopInventoryItem().getName().equalsIgnoreCase(STORE_INVENTORY) &&
                        slot.getName().equalsIgnoreCase(PLAYER_INVENTORY) ) {
                    _fullValue += slot.getTopInventoryItem().getItemValue();
                    _buyTotalLabel.setText(BUY  + " : " +  _fullValue + GP);
                    if( _fullValue > 0 ) {
                        _buyButton.setDisabled(false);
                        _buyButton.setTouchable(Touchable.enabled);
                    }
                }
                break;
            case REMOVED_ITEM:
                if( slot.getTopInventoryItem().getName().equalsIgnoreCase(PLAYER_INVENTORY) &&
                        slot.getName().equalsIgnoreCase(STORE_INVENTORY) ) {
                    _tradeInVal -= slot.getTopInventoryItem().getTradeValue();
                    _sellTotalLabel.setText(SELL  + " : " +  _tradeInVal + GP);
                    if( _tradeInVal <= 0 ) {
                        _sellButton.setDisabled(true);
                        _sellButton.setTouchable(Touchable.disabled);
                    }
                }
                if( slot.getTopInventoryItem().getName().equalsIgnoreCase(STORE_INVENTORY) &&
                        slot.getName().equalsIgnoreCase(PLAYER_INVENTORY) ) {
                    _fullValue -= slot.getTopInventoryItem().getItemValue();
                    _buyTotalLabel.setText(BUY  + " : " +  _fullValue + GP);
                    if( _fullValue <= 0 ) {
                        _buyButton.setDisabled(true);
                        _buyButton.setTouchable(Touchable.disabled);
                    }
                }

                break;
        }
    }
}
