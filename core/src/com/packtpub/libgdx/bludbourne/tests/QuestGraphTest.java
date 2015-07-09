package com.packtpub.libgdx.bludbourne.tests;

import com.packtpub.libgdx.bludbourne.quest.QuestGraph;
import com.packtpub.libgdx.bludbourne.quest.QuestTask;
import com.packtpub.libgdx.bludbourne.quest.QuestTaskDependency;

import java.util.Hashtable;

public class QuestGraphTest {
    static Hashtable<String, QuestTask> _questTasks;
    static QuestGraph _graph;
    static String quit = "q";
    static String _input = "";


    public static void main (String[] arg) {
        _questTasks = new Hashtable<String, QuestTask>();

        QuestTask firstTask = new QuestTask();
        firstTask.setId("500");
        firstTask.setTaskPhrase("Come back to me with the bones");

        QuestTask secondTask = new QuestTask();
        secondTask.setId("601");
        secondTask.setTaskPhrase("Pickup 5 bones from the Isle of Death");

        _questTasks.put(firstTask.getId(), firstTask);
        _questTasks.put(secondTask.getId(), secondTask);

        _graph = new QuestGraph();
        _graph.setTasks(_questTasks);

        QuestTaskDependency firstDep = new QuestTaskDependency();
        firstDep.setSourceId(firstTask.getId());
        firstDep.setDestinationId(secondTask.getId());

        QuestTaskDependency cycleDep = new QuestTaskDependency();
        cycleDep.setSourceId(secondTask.getId());
        cycleDep.setDestinationId(firstTask.getId());

        _graph.addDependency(firstDep);
        _graph.addDependency(cycleDep);

        System.out.println(_graph.toString());
    }
}
