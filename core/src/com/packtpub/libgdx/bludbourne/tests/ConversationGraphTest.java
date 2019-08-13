package com.packtpub.libgdx.bludbourne.tests;


import com.packtpub.libgdx.bludbourne.dialog.Conversation;
import com.packtpub.libgdx.bludbourne.dialog.ConversationChoice;
import com.packtpub.libgdx.bludbourne.dialog.ConversationGraph;

import java.util.ArrayList;
import java.util.Hashtable;

public class ConversationGraphTest {
    static Hashtable<String, Conversation> _conversations;
    static ConversationGraph _graph;
    static String quit = "q";
    static String _input = "";

    public static void main (String[] arg) {
        _conversations = new Hashtable<String, Conversation>();

        Conversation start = new Conversation();
        start.setId("500");
        start.setDialog("Do you want to play a game?");

        Conversation yesAnswer = new Conversation();
        yesAnswer.setId("601");
        yesAnswer.setDialog("BOOM! Bombs dropping everywhere");

        Conversation noAnswer = new Conversation();
        noAnswer.setId("802");
        noAnswer.setDialog("Too bad!");

        Conversation unconnectedTest = new Conversation();
        unconnectedTest.setId("250");
        unconnectedTest.setDialog("I am unconnected");

        _conversations.put(start.getId(), start);
        _conversations.put(noAnswer.getId(), noAnswer);
        _conversations.put(yesAnswer.getId(), yesAnswer);
        _conversations.put(unconnectedTest.getId(), unconnectedTest);

        _graph = new ConversationGraph(_conversations, start.getId());

        ConversationChoice yesChoice = new ConversationChoice();
        yesChoice.setSourceId(start.getId());
        yesChoice.setDestinationId(yesAnswer.getId());
        yesChoice.setChoicePhrase("YES");

        ConversationChoice noChoice = new ConversationChoice();
        noChoice.setSourceId(start.getId());
        noChoice.setDestinationId(noAnswer.getId());
        noChoice.setChoicePhrase("NO");

        ConversationChoice startChoice01 = new ConversationChoice();
        startChoice01.setSourceId(yesAnswer.getId());
        startChoice01.setDestinationId(start.getId());
        startChoice01.setChoicePhrase("Go to beginning!");

        ConversationChoice startChoice02 = new ConversationChoice();
        startChoice02.setSourceId(noAnswer.getId());
        startChoice02.setDestinationId(start.getId());
        startChoice02.setChoicePhrase("Go to beginning!");

        _graph.addChoice(yesChoice);
        _graph.addChoice(noChoice);
        _graph.addChoice(startChoice01);
        _graph.addChoice(startChoice02);

        //System.out.println(_graph.toString());
        //System.out.println(_graph.displayCurrentConversation());
        //System.out.println(_graph.toJson());

        while( !_input.equalsIgnoreCase(quit) ){
            Conversation conversation = getNextChoice();
            if( conversation == null ) continue;
            _graph.setCurrentConversation(conversation.getId());
            //System.out.println(_graph.displayCurrentConversation());
        }
    }

    public static Conversation getNextChoice(){
        ArrayList<ConversationChoice> choices = _graph.getCurrentChoices();
        for(ConversationChoice choice: choices){
            //System.out.println(choice.getDestinationId() + " " + choice.getChoicePhrase());
        }
        _input = System.console().readLine();

        Conversation choice = null;
        try {
            choice = _graph.getConversationByID(_input);
        }catch( NumberFormatException nfe){
            return null;
        }
        return choice;
    }

}
