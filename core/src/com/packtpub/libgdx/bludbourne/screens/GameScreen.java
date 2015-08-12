package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.audio.AudioManager;
import com.packtpub.libgdx.bludbourne.audio.AudioObserver;
import com.packtpub.libgdx.bludbourne.audio.AudioSubject;

public class GameScreen implements Screen, AudioSubject{
    private Array<AudioObserver> _observers;

    public GameScreen(){
        _observers = new Array<AudioObserver>();
        this.addObserver(AudioManager.getInstance());
    }

    @Override
    public void addObserver(AudioObserver audioObserver) {
        _observers.add(audioObserver);
    }

    @Override
    public void removeObserver(AudioObserver audioObserver) {
        _observers.removeValue(audioObserver, true);
    }

    @Override
    public void removeAllObservers() {
        _observers.removeAll(_observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer: _observers){
            observer.onNotify(command, event);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
