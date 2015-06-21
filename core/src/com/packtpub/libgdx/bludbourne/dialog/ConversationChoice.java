package com.packtpub.libgdx.bludbourne.dialog;


public class ConversationChoice {
    private int sourceId = 0;
    private int destinationId = 0;
    private String choicePhrase = "";

    public ConversationChoice(){

    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId){
        this.sourceId = sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId){
        this.destinationId = destinationId;
    }

    public String getChoicePhrase() {
        return choicePhrase;
    }

    public void setChoicePhrase(String choicePhrase) {
        this.choicePhrase = choicePhrase;
    }
}
