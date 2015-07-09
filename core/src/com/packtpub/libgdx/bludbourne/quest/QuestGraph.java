package com.packtpub.libgdx.bludbourne.quest;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class QuestGraph {
    private Hashtable<String, QuestTask> questTasks;
    private Hashtable<String, ArrayList<QuestTaskDependency>> questTaskDependencies;

    public void setTasks(Hashtable<String, QuestTask> questTasks) {
        if( questTasks.size() < 0 ){
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this.questTasks = questTasks;
        this.questTaskDependencies = new Hashtable<String, ArrayList<QuestTaskDependency>>(questTasks.size());

        for( QuestTask questTask: questTasks.values() ){
            questTaskDependencies.put(questTask.getId(), new ArrayList<QuestTaskDependency>());
        }
    }

    public boolean isValid(String taskID){
        QuestTask questTask = questTasks.get(taskID);
        if( questTask == null ) return false;
        return true;
    }

    public boolean isReachable(String sourceID, String sinkID){
        if( !isValid(sourceID) || !isValid(sinkID) ) return false;
        if( questTasks.get(sourceID) == null ) return false;

        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(sourceID);
        if( list == null ) return false;
        for(QuestTaskDependency dependency: list){
            if(     dependency.getSourceId().equalsIgnoreCase(sourceID) &&
                    dependency.getDestinationId().equalsIgnoreCase(sinkID) ){
                return true;
            }
        }
        return false;
    }

    public QuestTask getQuestTaskByID(String id){
        if( !isValid(id) ){
            System.out.println("Id " + id + " is not valid!");
            return null;
        }
        return questTasks.get(id);
    }

    public void addDependency(QuestTaskDependency questTaskDependency){
        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(questTaskDependency.getSourceId());
        if( list == null) return;

        //Will not add if creates cycles
        if( doesCycleExist(questTaskDependency) ){
            System.out.println("Cycle exists! Not adding");
            return;
        }

        list.add(questTaskDependency);
    }

    public boolean doesCycleExist(QuestTaskDependency questTaskDep){
        Set<String> keys = questTasks.keySet();
        for( String id: keys ){
            if( doesQuestTaskHaveDependencies(id) &&
                    questTaskDep.getDestinationId().equalsIgnoreCase(id)){
                    System.out.println("ID: " + id + " destID: " + questTaskDep.getDestinationId());
                    return true;
                }
            }
        return false;
    }

    public boolean doesQuestTaskHaveDependencies(String id){
        QuestTask task = getQuestTaskByID(id);
        if( task == null) return false;
        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(id);

        if( list.isEmpty() || list.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String toString(){
        StringBuilder outputString = new StringBuilder();
        int numberTotalChoices = 0;

        Set<String> keys = questTaskDependencies.keySet();
        for( String id: keys){
            outputString.append(String.format("[%s]: ", id));
            outputString.append(String.format("[%s]: ", getQuestTaskByID(id).getTaskPhrase()));

            for( QuestTaskDependency dependency: questTaskDependencies.get(id)){
                numberTotalChoices++;
                outputString.append(String.format("%s ", dependency.getDestinationId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        outputString.append(String.format("Number quest tasks: %d", questTasks.size()));
        outputString.append(String.format(", Number of dependencies: %d", numberTotalChoices));
        outputString.append(System.getProperty("line.separator"));

        return outputString.toString();
    }

    public String toJson(){
        Json json = new Json();
        return json.prettyPrint(this);
    }

}
