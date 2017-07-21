package com.colisa.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.screens.DirectedGame;
import com.colisa.canyonbunny.screens.MenuScreen;
import com.colisa.canyonbunny.screens.transitions.ScreenTransition;
import com.colisa.canyonbunny.screens.transitions.ScreenTransitionSlice;
import com.colisa.canyonbunny.util.AudioManager;
import com.colisa.canyonbunny.util.GamePreferences;

public class CanyonBunnyMain extends DirectedGame {
    private static final String TAG = CanyonBunnyMain.class.getName();

    @Override
    public void create() {
        // Set LIBGDX log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.assetMusic.song01);

        // start game at menu screen
        ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
        setScreen(new MenuScreen(this), transition);
        Gdx.app.debug(TAG, "Game created, initially setting menu screen.");
    }
}

