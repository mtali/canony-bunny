package com.colisa.canyonbunny.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

    public static final GamePreferences instance = new GamePreferences();
    public static final String TAG = GamePreferences.class.getName();

    public boolean sound;
    public boolean music;
    public float volumeSound;
    public float volumeMusic;
    public int characterSkin;
    public boolean showFPSCounter;

    private Preferences preferences;

    private GamePreferences() {
        preferences = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load(){
        sound = preferences.getBoolean("sound", true);
        music = preferences.getBoolean("music", true);
        volumeSound = MathUtils.clamp(preferences.getFloat("volumeSound", 0.5f), 0.0f, 1.0f);
        volumeMusic = MathUtils.clamp(preferences.getFloat("volumeMusic", 0.5f), 0.0f, 1.0f);
        characterSkin = MathUtils.clamp(preferences.getInteger("characterSkin", 0), 0, 2);
        showFPSCounter = preferences.getBoolean("showFPSCounter", false);
    }
    public void save(){
        preferences.putBoolean("sound", sound);
        preferences.putBoolean("music", music);
        preferences.putFloat("volumeSound", volumeSound);
        preferences.putFloat("volumeMusic", volumeMusic);
        preferences.putInteger("characterSkin", characterSkin);
        preferences.putBoolean("showFPSCounter", showFPSCounter);
        preferences.flush();
    }
}
