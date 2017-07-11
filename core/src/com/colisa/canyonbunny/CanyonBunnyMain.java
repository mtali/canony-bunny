package com.colisa.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.game.WorldController;
import com.colisa.canyonbunny.game.WorldRenderer;

public class CanyonBunnyMain extends ApplicationAdapter {
    private static final String TAG = CanyonBunnyMain.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    // Handle game pause
    private boolean paused;

    @Override
    public void create() {
        // Set libgdx log level to debug
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Initialize world controller and world renderer
        worldController = new com.colisa.canyonbunny.game.WorldController();
        worldRenderer = new WorldRenderer(worldController);

        // Game world is active to start
        paused = false;
    }

    @Override
    public void render() {
        // Do not update the game world when paused
        if (!paused) {
            // Update the world by the time that has passed since last render
            worldController.update(Gdx.graphics.getDeltaTime());
        }


        // Set the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game world to screen
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
        Assets.instance.dispose();
    }
}

