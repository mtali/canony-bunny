package com.colisa.canyonbunny.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.colisa.canyonbunny.game.Assets;

public abstract class AbstractGameScreen implements Screen {
    private static final String TAG = AbstractGameScreen.class.getName();
    protected DirectedGame game;

    public AbstractGameScreen(DirectedGame game){
        this.game = game;
    }

    public abstract void render(float deltaTime);
    public abstract void resize(int width, int height);
    public abstract void show();
    public abstract void hide();
    public abstract void pause();

    public void resume(){
        Assets.instance.init(new AssetManager());
        Gdx.app.debug(TAG, "resume called. Initializing assets");
    }

    public void dispose(){
        Assets.instance.dispose();
        Gdx.app.debug(TAG, "dispose() called; Disposing assets");
    }

    public abstract InputProcessor getInputProcessor();

}
