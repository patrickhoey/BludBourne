package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
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

    private Json _json;

    public ConversationUI() {
        super("dialog", Utility.STATUSUI_SKIN, "solidbackground");

        _json = new Json();
        _graph = new ConversationGraph();
        this.setFillParent(true);

        //create
        _dialogText = new Label("No Conversation", Utility.STATUSUI_SKIN);
        _dialogText.setWrap(true);
        _listItems = new List<ConversationChoice>(Utility.STATUSUI_SKIN);

        ScrollPane scrollPane = new ScrollPane(_listItems);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);
        //_dialogTable.setBackground(new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))).getDrawable());

        //layout
        this.defaults().expand().fill();
        this.add(_dialogText).pad(10,10,10,10);
        this.row();
        this.add(scrollPane);

        //this.debug();
        this.pack();

        //Listeners
        scrollPane.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       ConversationChoice choice = (ConversationChoice)_listItems.getSelected();
                                       _graph.setCurrentConversation(choice.getDestinationId());
                                       _dialogText.setText(_graph.getConversationByID(choice.getDestinationId()).getDialog());
                                       _listItems.setItems(_graph.getCurrentChoices().toArray());
                                       return true;
                                   }
                               }
        );
    }

    public String getCurrentEntityID() {
        return _currentEntityID;
    }

    public void loadConversation(EntityConfig entityConfig){
        Json json = new Json();
        String fullFilenamePath = entityConfig.getConversationConfigPath();
        _currentEntityID = entityConfig.getEntityID();
        if( fullFilenamePath.isEmpty() || !Gdx.files.internal(fullFilenamePath).exists() ){
            Gdx.app.debug(TAG, "Conversation file does not exist!");
            return;
        }

        ConversationGraph graph = json.fromJson(ConversationGraph.class, Gdx.files.internal(fullFilenamePath));
        setConversationGraph(graph);
    }

    public void setConversationGraph(ConversationGraph graph){
        this._graph = graph;
        String id = _graph.getCurrentConversationID();
        Conversation conversation = _graph.getConversationByID(id);
        if( conversation == null ) return;
        this._dialogText.setText(conversation.getDialog());
        this._listItems.setItems(_graph.getCurrentChoices().toArray());
    }

}
