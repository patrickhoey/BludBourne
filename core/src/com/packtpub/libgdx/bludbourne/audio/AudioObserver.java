package com.packtpub.libgdx.bludbourne.audio;

public interface AudioObserver {
    public static enum AudioTypeEvent{
        MUSIC_TITLE("core/assets/audio/10112013.ogg"),
        MUSIC_TOWN("core/assets/audio/Magic Town_0.mp3"),
        MUSIC_TOPWORLD("core/assets/audio/n3535n5n335n35nj.ogg"),
        MUSIC_CASTLEDOOM("core/assets/audio/Dark chamber.mp3"),
        MUSIC_BATTLE("core/assets/audio/Random Battle.mp3"),
        MUSIC_INTRO_CUTSCENE("core/assets/audio/Takeover_5.mp3"),
        MUSIC_LEVEL_UP_FANFARE("core/assets/audio/4 Open Surge score jingle - B.ogg"),
        SOUND_CREATURE_PAIN("core/assets/audio/27780_SFX_CreatureGruntInPain1.wav"),
        SOUND_PLAYER_PAIN("core/assets/audio/27678_SFX_ComicalSoundsTiredGrunt1.wav"),
        SOUND_PLAYER_WAND_ATTACK("core/assets/audio/26230_SFX_ProductionElementReverseWhoosh19.wav"),
        SOUND_EATING("core/assets/audio/17661_SFX_HumanEatingPotatoChips1.wav"),
        SOUND_DRINKING("core/assets/audio/27677_SFX_ComicalSoundsSwallowLiquid1.wav"),
        SOUND_COIN_RUSTLE("core/assets/audio/00954_SFX_MoneyCoinsDumpedInHand_final.wav"),
        NONE("");

        private String _audioFullFilePath;

        AudioTypeEvent(String audioFullFilePath){
            this._audioFullFilePath = audioFullFilePath;
        }

        public String getValue(){
            return _audioFullFilePath;
        }
    }

    public static enum AudioCommand {
        MUSIC_LOAD,
        MUSIC_PLAY_ONCE,
        MUSIC_PLAY_LOOP,
        MUSIC_STOP,
        MUSIC_STOP_ALL,
        SOUND_LOAD,
        SOUND_PLAY_ONCE,
        SOUND_PLAY_LOOP,
        SOUND_STOP
    }

    void onNotify(AudioCommand command, AudioTypeEvent event);
}
