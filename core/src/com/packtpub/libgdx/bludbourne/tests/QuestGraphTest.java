package com.packtpub.libgdx.bludbourne.tests;

import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.quest.QuestGraph;
import com.packtpub.libgdx.bludbourne.quest.QuestTask;
import com.packtpub.libgdx.bludbourne.quest.QuestTaskDependency;

import java.util.Hashtable;

public class QuestGraphTest {
    static Hashtable<String, QuestTask> _questTasks;
    static QuestGraph _graph;
    static Json _json;
    static String quit = "q";
    static String _input = "";


    public static void main (String[] arg) {
        _json = new Json();

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

        _questTasks.clear();
        _graph.clear();

        QuestTask q1 = new QuestTask();
        q1.setId("1");
        q1.setTaskPhrase("Come back to me with the items");

        QuestTask q2 = new QuestTask();
        q2.setId("2");
        q2.setTaskPhrase("Collect 5 horns");

        QuestTask q3 = new QuestTask();
        q3.setId("3");
        q3.setTaskPhrase("Collect 5 furs");

        QuestTask q4 = new QuestTask();
        q4.setId("4");
        q4.setTaskPhrase("Find the area where the Tuskan beast feasts");

        _questTasks.put(q1.getId(), q1);
        _questTasks.put(q2.getId(), q2);
        _questTasks.put(q3.getId(), q3);
        _questTasks.put(q4.getId(), q4);

        _graph.setTasks(_questTasks);

        QuestTaskDependency qDep1 = new QuestTaskDependency();
        qDep1.setSourceId(q1.getId());
        qDep1.setDestinationId(q2.getId());

        QuestTaskDependency qDep2 = new QuestTaskDependency();
        qDep2.setSourceId(q1.getId());
        qDep2.setDestinationId(q3.getId());

        QuestTaskDependency qDep3 = new QuestTaskDependency();
        qDep3.setSourceId(q2.getId());
        qDep3.setDestinationId(q4.getId());

        QuestTaskDependency qDep4 = new QuestTaskDependency();
        qDep4.setSourceId(q3.getId());
        qDep4.setDestinationId(q4.getId());

        _graph.addDependency(qDep1);
        _graph.addDependency(qDep2);
        _graph.addDependency(qDep3);
        _graph.addDependency(qDep4);

        System.out.println(_json.prettyPrint(_graph));

        _questTasks.clear();
        _graph.clear();

        QuestTask q01 = new QuestTask();
        q01.setId("1");
        q01.setTaskPhrase("Come back to me with the herbs");
        q01.resetAllProperties();

        QuestTask q02 = new QuestTask();
        q02.setId("2");
        q02.setTaskPhrase("Please collect 5 herbs for my sick mother");
        q02.resetAllProperties();

        _questTasks.put(q01.getId(), q01);
        _questTasks.put(q02.getId(), q02);

        _graph.setTasks(_questTasks);

        QuestTaskDependency qDep01 = new QuestTaskDependency();
        qDep01.setSourceId(q01.getId());
        qDep01.setDestinationId(q02.getId());

        _graph.addDependency(qDep01);

        System.out.println(_json.prettyPrint(_graph));


    }
}
