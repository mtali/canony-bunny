package com.colisa.canyonbunny.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static final AudioManager instance = new AudioManager();
    private static final String TAG = AudioManager.class.getName();

    private Music playingMusic;

    private AudioManager() {
    }

    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.volumeSound * volume, pitch, pan);
    }

    public void play(Music music) {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.volumeMusic);
            music.play();
        }
    }

    public void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    public void onSettingsUpdated() {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.volumeMusic);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}
