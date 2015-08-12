package com.packtpub.libgdx.bludbourne.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.packtpub.libgdx.bludbourne.Utility;

import java.util.Hashtable;

public class AudioManager implements AudioObserver {
    private static final String TAG = AudioManager.class.getSimpleName();

    private static AudioManager _instance = null;

    private Hashtable<String, Music> _queuedMusic;
    private Hashtable<String, Sound> _queuedSounds;

    private AudioManager(){
        _queuedMusic = new Hashtable<String, Music>();
        _queuedSounds = new Hashtable<String, Sound>();

    }

    public static AudioManager getInstance() {
        if (_instance == null) {
            _instance = new AudioManager();
        }

        return _instance;
    }


    @Override
    public void onNotify(AudioCommand command, AudioTypeEvent event) {
        switch(command){
            case MUSIC_LOAD:
                Utility.loadMusicAsset(event.getValue());
                break;
            case MUSIC_PLAY_ONCE:
                playMusic(false, event.getValue());
                break;
            case MUSIC_PLAY_LOOP:
                playMusic(true, event.getValue());
                break;
            case MUSIC_STOP:
                Music music = _queuedMusic.get(event.getValue());
                if( music != null ){
                    music.stop();
                }
                break;
            case SOUND_LOAD:
                Utility.loadSoundAsset(event.getValue());
                break;
            case SOUND_PLAY_LOOP:
                playSound(true, event.getValue());
                break;
            case SOUND_PLAY_ONCE:
                playSound(false, event.getValue());
                break;
            case SOUND_STOP:
                Sound sound = _queuedSounds.get(event.getValue());
                if( sound != null ){
                    sound.stop();
                }
                break;
            default:
                break;
        }
    }

    private void playMusic(boolean isLooping, String fullFilePath){
        Music music = _queuedMusic.get(fullFilePath);
        if( music != null ){
            music.setLooping(isLooping);
            music.play();
        }else if(Utility.isAssetLoaded(fullFilePath)){
            Music asset = Utility.getMusicAsset(fullFilePath);
            asset.setLooping(isLooping);
            asset.play();
            _queuedMusic.put(fullFilePath, asset);
        }else{
            Gdx.app.debug(TAG, "Music not loaded");
            return;
        }
    }

    private void playSound(boolean isLooping, String fullFilePath){
        Sound sound = _queuedSounds.get(fullFilePath);
        if( sound != null ){
            long soundId = sound.play();
            sound.setLooping(soundId, isLooping);
        }else if( Utility.isAssetLoaded(fullFilePath) ) {
            Sound asset = Utility.getSoundAsset(fullFilePath);
            long soundId = asset.play();
            asset.setLooping(soundId, isLooping);
            _queuedSounds.put(fullFilePath, asset);
        }else{
            Gdx.app.debug(TAG, "Sound not loaded");
            return;
        }
    }

}
