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

public class CanyonBunnyMain extends DirectedGame {
    private static final String TAG = CanyonBunnyMain.class.getName();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Assets.instance.init(new AssetManager());

        // start game at menu screen
        ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
        setScreen(new MenuScreen(this), transition);
        Gdx.app.debug(TAG, "Game created, initially setting menu screen.");
    }
}

