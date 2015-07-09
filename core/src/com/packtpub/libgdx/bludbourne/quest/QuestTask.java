package com.packtpub.libgdx.bludbourne.quest;


import com.badlogic.gdx.utils.ObjectMap;

public class QuestTask {
    private final String IS_TASK_COMPLETE = "IS_TASK_COMPLETE";

    private ObjectMap<String, Object> taskProperties;
    private String id;
    private String taskPhrase;

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTaskPhrase() {
        return taskPhrase;
    }

    public void setTaskPhrase(String taskPhrase) {
        this.taskPhrase = taskPhrase;
    }

    public ObjectMap<String, Object> getTaskProperties() {
        return taskProperties;
    }

    public void setTaskProperties(ObjectMap<String, Object> taskProperties) {
        this.taskProperties = taskProperties;
    }

    public boolean isTaskComplete(){
        if( !taskProperties.containsKey(IS_TASK_COMPLETE) ){
            taskProperties.put(IS_TASK_COMPLETE, "false");
            return false;
        }
        return Boolean.getBoolean(taskProperties.get(IS_TASK_COMPLETE).toString());
    }



}
