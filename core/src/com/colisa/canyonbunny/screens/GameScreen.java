package com.colisa.canyonbunny.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colisa.canyonbunny.game.WorldController;
import com.colisa.canyonbunny.game.WorldRenderer;
import com.colisa.canyonbunny.util.GamePreferences;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean paused;

    public GameScreen(DirectedGame game){
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused){
            worldController.update(deltaTime);
        }
        worldRenderer.render();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        worldController.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }


    @Override
    public void resume() {
        super.resume();
        paused = false;
    }
}
