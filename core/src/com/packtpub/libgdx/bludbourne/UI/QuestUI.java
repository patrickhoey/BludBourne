package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.quest.QuestGraph;
import com.packtpub.libgdx.bludbourne.quest.QuestTask;

import java.util.ArrayList;

public class QuestUI extends Window {
    private static final String TAG = QuestUI.class.getSimpleName();

    public static final String RETURN_QUEST = "conversations/return_quest.json";
    public static final String FINISHED_QUEST = "conversations/quest_finished.json";

    private List _listQuests;
    private List _listTasks;
    private Json _json;
    private Array<QuestGraph> _quests;
    private Label _questLabel;
    private Label _tasksLabel;

    public QuestUI() {
        super("Quest Log", Utility.STATUSUI_SKIN, "solidbackground");

        _json = new Json();
        _quests = new Array<QuestGraph>();

        //create
        _questLabel = new Label("Quests:", Utility.STATUSUI_SKIN);
        _tasksLabel = new Label("Tasks:", Utility.STATUSUI_SKIN);

        _listQuests = new List<QuestGraph>(Utility.STATUSUI_SKIN);

        ScrollPane scrollPane = new ScrollPane(_listQuests, Utility.STATUSUI_SKIN, "inventoryPane");
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(true, false);

        _listTasks = new List<QuestTask>(Utility.STATUSUI_SKIN);

        ScrollPane scrollPaneTasks = new ScrollPane(_listTasks, Utility.STATUSUI_SKIN, "inventoryPane");
        scrollPaneTasks.setOverscroll(false, false);
        scrollPaneTasks.setFadeScrollBars(false);
        scrollPaneTasks.setForceScroll(true, false);

        //layout
        this.add(_questLabel).align(Align.left);
        this.add(_tasksLabel).align(Align.left);
        this.row();
        this.defaults().expand().fill();
        this.add(scrollPane).padRight(5);
        this.add(scrollPaneTasks).padLeft(5);

        //this.debug();
        this.pack();

        //Listeners
        _listQuests.addListener(new ClickListener() {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       QuestGraph quest = (QuestGraph) _listQuests.getSelected();
                                       if (quest == null) return;
                                       populateQuestDialog(quest);
                                   }
                               }
        );
    }

    public boolean addQuest(String questConfigPath){
        if( questConfigPath.isEmpty() || !Gdx.files.internal(questConfigPath).exists() ){
            Gdx.app.debug(TAG, "Quest file does not exist!");
            return false;
        }

        QuestGraph graph = _json.fromJson(QuestGraph.class, Gdx.files.internal(questConfigPath));
        if( doesQuestAlreadyExist(graph) ){
            return false;
        }

        clearDialog();
        _quests.add(graph);
        updateQuestItemList();
        return true;
    }

    public boolean doesQuestAlreadyExist(QuestGraph graph){
        for( QuestGraph questGraph: _quests ){
            if( questGraph.getQuestID().equalsIgnoreCase(graph.getQuestID())){
                return true;
            }
        }
        return false;
    }

    public Array<QuestGraph> getQuests() {
        return _quests;
    }

    public void setQuests(Array<QuestGraph> quests) {
        this._quests = quests;
        updateQuestItemList();
    }

    public void updateQuestItemList(){
        clearDialog();

        _listQuests.setItems(_quests);
        _listQuests.setSelectedIndex(-1);
    }

    private void clearDialog(){
        _listQuests.clearItems();
    }

    private void populateQuestDialog(QuestGraph graph){
        _listTasks.clearItems();

        ArrayList<QuestTask> tasks =  graph.getAllQuestTasks();
        if( tasks == null ) return;

        _listTasks.setItems(tasks.toArray());
        _listTasks.setSelectedIndex(-1);
    }

    public void initQuests(MapManager mapMgr){
        mapMgr.clearAllMapQuestEntities();

        //populate items if quests have them
        for( QuestGraph quest : _quests ){
            if( !quest.isQuestComplete() ){
                quest.init(mapMgr);
            }
        }
    }

    public void updateQuests(MapManager mapMgr){
        for( QuestGraph quest : _quests ){
            if( !quest.isQuestComplete() ){
                quest.update(mapMgr);
            }
        }
    }

}
