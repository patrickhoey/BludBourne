package com.packtpub.libgdx.bludbourne.dialog;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class ConversationGraph {
    private int _numChoices = 0;
    private Hashtable<Integer, Conversation> _conversations;
    private Hashtable<Integer, ArrayList<ConversationChoice>> _associatedChoices;
    private Conversation _currentConversation = null;

    public ConversationGraph(Hashtable<Integer, Conversation> conversations, int rootID){
        if( conversations.size() < 0 ){
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this._conversations = conversations;
        this._currentConversation = getConversationByID(rootID);
        this._numChoices = 0;
        this._associatedChoices = new Hashtable(_conversations.size());

        for( Conversation conversation: _conversations.values() ){
            _associatedChoices.put(conversation.getId(), new ArrayList<ConversationChoice>());
       }
    }

    public ArrayList<ConversationChoice> getCurrentChoices(){
        return _associatedChoices.get(_currentConversation.getId());
    }

    public void setCurrentConversation(int id){
        Conversation conversation = getConversationByID(id);
        if( conversation == null ) return;
        //Can we reach the new conversation from the current one?
        if( isReachable(_currentConversation.getId(), id) ){
            _currentConversation = conversation;
        }else{
            System.out.println("New conversation node is not reachable from current node!");
        }
    }

    public boolean isValid(int conversationID){
        Conversation conversation = _conversations.get(conversationID);
        if( conversation == null ) return false;
        return true;
    }

    public boolean isReachable(int sourceID, int sinkID){
        if( !isValid(sourceID) || !isValid(sinkID) ) return false;
        if( _conversations.get(sourceID) == null ) return false;

        //First get edges/choices from the source
        ArrayList<ConversationChoice> list = _associatedChoices.get(sourceID);
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
        return _conversations.get(id);
    }

    public String getDestinationChoicePhraseByID(int id){
        if( isReachable(_currentConversation.getId(), id) ){
            ArrayList<ConversationChoice> list = _associatedChoices.get(_currentConversation.getId());
            for(ConversationChoice choice: list){
                if( choice.getDestinationId() == id ){
                    return choice.getChoicePhrase();
                }
            }
        }
        return "";
    }

    public String displayCurrentConversation(){
        return _currentConversation.getDialog();
    }

    public int getNumConversations() {
        return _conversations.size();
    }

    public int getNumChoices() {
        return _numChoices;
    }

    public void addChoice(ConversationChoice conversationChoice){

        ArrayList<ConversationChoice> list = _associatedChoices.get(conversationChoice.getSourceId());
        if( list == null) return;

        list.add(conversationChoice);
        _numChoices++;
    }


    public String toString(){
        StringBuilder outputString = new StringBuilder();
        outputString.append("Number conversations: " + _conversations.size() + ", Number of choices:" + _numChoices);
        outputString.append(System.getProperty("line.separator"));

        Set<Integer> keys = _associatedChoices.keySet();
        for( Integer id: keys){
            outputString.append(String.format("[%d]: ", id));

            for( ConversationChoice choice: _associatedChoices.get(id)){
                outputString.append(String.format("%d ", choice.getDestinationId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        return outputString.toString();
    }


}
