package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.EntityConfig;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.dialog.Conversation;
import com.packtpub.libgdx.bludbourne.dialog.ConversationChoice;
import com.packtpub.libgdx.bludbourne.dialog.ConversationGraph;

public class ConversationUI extends Window {
    private static final String TAG = ConversationUI.class.getSimpleName();

    private Label _dialogText;
    private List _listItems;
    private ConversationGraph _graph;
    private String _currentEntityID;

    public TextButton _closeButton;

    private Json _json;

    public ConversationUI() {
        super("dialog", Utility.STATUSUI_SKIN, "solidbackground");

        _json = new Json();
        _graph = new ConversationGraph();

        //create
        _dialogText = new Label("No Conversation", Utility.STATUSUI_SKIN);
        _dialogText.setWrap(true);
        _dialogText.setAlignment(Align.center);
        _listItems = new List<ConversationChoice>(Utility.STATUSUI_SKIN);

        _closeButton = new TextButton("X", Utility.STATUSUI_SKIN);

        ScrollPane scrollPane = new ScrollPane(_listItems, Utility.STATUSUI_SKIN, "inventoryPane");
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        //layout
        this.add();
        this.add(_closeButton);
        this.row();

        this.defaults().expand().fill();
        this.add(_dialogText).pad(10, 10, 10, 10);
        this.row();
        this.add(scrollPane);

        //this.debug();
        this.pack();

        //Listeners
        _listItems.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                ConversationChoice choice = (ConversationChoice)_listItems.getSelected();
                _graph.setCurrentConversation(choice.getDestinationId());
                _dialogText.setText(_graph.getConversationByID(choice.getDestinationId()).getDialog());
                _listItems.setItems(_graph.getCurrentChoices().toArray());
                _listItems.setSelectedIndex(-1);
            }
                               }
        );
    }

    public String getCurrentEntityID() {
        return _currentEntityID;
    }

    public void loadConversation(EntityConfig entityConfig){
        String fullFilenamePath = entityConfig.getConversationConfigPath();
        if( fullFilenamePath.isEmpty() || !Gdx.files.internal(fullFilenamePath).exists() ){
            Gdx.app.debug(TAG, "Conversation file does not exist!");
            _dialogText.setText("");
            _listItems.clearItems();
            return;
        }

        _currentEntityID = entityConfig.getEntityID();
        Json json = new Json();
        ConversationGraph graph = json.fromJson(ConversationGraph.class, Gdx.files.internal(fullFilenamePath));
        setConversationGraph(graph);
    }

    public void setConversationGraph(ConversationGraph graph){
        this._graph = graph;
        String id = _graph.getCurrentConversationID();
        Conversation conversation = _graph.getConversationByID(id);
        if( conversation == null ) return;
        _dialogText.setText(conversation.getDialog());
        _listItems.setItems(_graph.getCurrentChoices().toArray());
        _listItems.setSelectedIndex(-1);
    }

}
