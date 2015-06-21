package com.packtpub.libgdx.bludbourne.dialog;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class ConversationGraph {
    private int _numChoices = 0;
    private Hashtable<Integer, Conversation> _conversations;
    private Hashtable<Conversation, ArrayList<Conversation>> _associatedChoices;
    private Conversation _currentConversation = null;

    public ConversationGraph(Hashtable<Integer, Conversation> conversations, Conversation root){
        if( conversations.size() < 0 ){
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this._conversations = conversations;
        this._currentConversation = root;
        this._numChoices = 0;
        this._associatedChoices = new Hashtable(_conversations.size());

        for( Conversation conversation: _conversations.values() ){
            _associatedChoices.put(conversation, new ArrayList<Conversation>());
       }
    }

    public ArrayList<Conversation> getCurrentChoices(){
        return _associatedChoices.get(_currentConversation);
    }

    public void setCurrentConversation(Conversation conversation){
        if( conversation == null || _associatedChoices.get(conversation) == null ) return;
        //Can we reach the new conversation from the current one?
        if( isReachable(_currentConversation, conversation) ){
            _currentConversation = conversation;
        }else{
            System.out.println("New conversation node is not reachable from current node!");
        }
    }

    public boolean isValid(Conversation conversation){
        if( conversation == null || _conversations.get(conversation.getId()) == null ) return false;
        return true;
    }

    public boolean isReachable(Conversation source, Conversation sink){
        if( !isValid(source) || !isValid(sink) ) return false;

        //First get edges/choices from the source
        ArrayList<Conversation> list = _associatedChoices.get(source);
        for(Conversation conversation: list){
            if( conversation.getId() == sink.getId() ){
                return true;
            }
        }
        return false;
    }

    public Conversation getConversationByID(int id){
        return _conversations.get(id);
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

    public void addChoice(Conversation sourceConversation, Conversation targetConversation){
        ArrayList<Conversation> list = _associatedChoices.get(sourceConversation);
        if( list == null) return;

        list.add(targetConversation);
        _numChoices++;
    }


    public String toString(){
        StringBuilder outputString = new StringBuilder();
        outputString.append("Number conversations: " + _conversations.size() + ", Number of choices:" + _numChoices);
        outputString.append(System.getProperty("line.separator"));

        Set<Conversation> keys = _associatedChoices.keySet();
        for( Conversation conversation: keys){
            outputString.append(String.format("[%d]: ", conversation.getId()));

            for( Conversation choices: _associatedChoices.get(conversation)){
                outputString.append(String.format("%d ", choices.getId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        return outputString.toString();
    }


}
