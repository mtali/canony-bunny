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
        if (worldController.isGameOver() && worldController.timeLeftGameOver < 0){
            worldController.backToMenu();
        }
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        Gdx.app.debug(TAG, "resize() called. Calling world render resize");
    }

    @Override
    public void show() {
        GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
        Gdx.app.debug(TAG, "show() called. Initializing world - controller and render");
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
        Gdx.app.debug(TAG, "hide() called. Calling world render dispose & Setting paused FALSE");
    }

    @Override
    public void pause() {
        paused = true;
        Gdx.app.debug(TAG, "pause() called. Setting paused TRUE");
    }


    @Override
    public void resume() {
        super.resume();
        paused = false;
        Gdx.app.debug(TAG, "resume() called. Setting paused FALSE");
    }
}
