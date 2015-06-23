package com.packtpub.libgdx.bludbourne.dialog;


public class ConversationChoice {
    private String sourceId;
    private String destinationId;
    private String choicePhrase;

    public ConversationChoice(){

    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId){
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId){
        this.destinationId = destinationId;
    }

    public String getChoicePhrase() {
        return choicePhrase;
    }

    public void setChoicePhrase(String choicePhrase) {
        this.choicePhrase = choicePhrase;
    }

    public String toString(){
        return choicePhrase;
    }
}
