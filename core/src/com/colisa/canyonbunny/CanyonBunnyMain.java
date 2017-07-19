package com.colisa.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.screens.DirectedGame;
import com.colisa.canyonbunny.screens.MenuScreen;

public class CanyonBunnyMain extends DirectedGame {
    private static final String TAG = CanyonBunnyMain.class.getName();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Assets.instance.init(new AssetManager());
        setScreen(new MenuScreen(this));
        Gdx.app.debug(TAG, "Game created, initially setting menu screen.");
    }
}

