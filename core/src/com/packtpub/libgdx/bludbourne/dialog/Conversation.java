package com.packtpub.libgdx.bludbourne.dialog;

public class Conversation {
    private int id = 0;
    private String dialog = "";
    private String choicePhrase = "";

    public Conversation(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDialog(){
        return dialog;
    }

    public void setDialog(String dialog){
        this.dialog = dialog;
    }

    public String getChoicePhrase() {
        return choicePhrase;
    }

    public void setChoicePhrase(String choicePhrase) {
        this.choicePhrase = choicePhrase;
    }
}
