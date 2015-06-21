package com.packtpub.libgdx.bludbourne.dialog;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class ConversationGraph {
    private Hashtable<Integer, Conversation> conversations;
    private Hashtable<Integer, ArrayList<ConversationChoice>> associatedChoices;
    private int currentConversationID = 0;

    public ConversationGraph(Hashtable<Integer, Conversation> conversations, int rootID){
        if( conversations.size() < 0 ){
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this.conversations = conversations;
        this.currentConversationID = rootID;
        this.associatedChoices = new Hashtable(conversations.size());

        for( Conversation conversation: conversations.values() ){
            associatedChoices.put(conversation.getId(), new ArrayList<ConversationChoice>());
       }
    }

    public ArrayList<ConversationChoice> getCurrentChoices(){
        return associatedChoices.get(currentConversationID);
    }

    public void setCurrentConversation(int id){
        Conversation conversation = getConversationByID(id);
        if( conversation == null ) return;
        //Can we reach the new conversation from the current one?
        if( isReachable(currentConversationID, id) ){
            currentConversationID = id;
        }else{
            System.out.println("New conversation node is not reachable from current node!");
        }
    }

    public boolean isValid(int conversationID){
        Conversation conversation = conversations.get(conversationID);
        if( conversation == null ) return false;
        return true;
    }

    public boolean isReachable(int sourceID, int sinkID){
        if( !isValid(sourceID) || !isValid(sinkID) ) return false;
        if( conversations.get(sourceID) == null ) return false;

        //First get edges/choices from the source
        ArrayList<ConversationChoice> list = associatedChoices.get(sourceID);
        for(ConversationChoice choice: list){
            if(     choice.getSourceId() == sourceID &&
                    choice.getDestinationId() == sinkID ){
                return true;
            }
        }
        return false;
    }

    public Conversation getConversationByID(int id){
        if( !isValid(id) ){
            System.out.println("Id " + id + " is not valid!");
            return null;
        }
        return conversations.get(id);
    }

    public String displayCurrentConversation(){
        return conversations.get(currentConversationID).getDialog();
    }

    public void addChoice(ConversationChoice conversationChoice){

        ArrayList<ConversationChoice> list = associatedChoices.get(conversationChoice.getSourceId());
        if( list == null) return;

        list.add(conversationChoice);
    }


    public String toString(){
        StringBuilder outputString = new StringBuilder();
        int numberTotalChoices = 0;

        Set<Integer> keys = associatedChoices.keySet();
        for( Integer id: keys){
            outputString.append(String.format("[%d]: ", id));

            for( ConversationChoice choice: associatedChoices.get(id)){
                numberTotalChoices++;
                outputString.append(String.format("%d ", choice.getDestinationId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        outputString.append("Number conversations: " + conversations.size() + ", Number of choices:" + numberTotalChoices);
        outputString.append(System.getProperty("line.separator"));

        return outputString.toString();
    }

    public String toJson(){
        Json json = new Json();
        return json.prettyPrint(this);
    }


}
