package com.colisa.canyonbunny.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.colisa.canyonbunny.screens.transitions.ScreenTransition;

public abstract class DirectedGame implements ApplicationListener {
    private static final String TAG = DirectedGame.class.getName();
    private boolean init;
    private AbstractGameScreen currentScreen;
    private AbstractGameScreen nextScreen;
    private FrameBuffer currentFBO;
    private FrameBuffer nextFBO;
    private SpriteBatch batch;
    private float t;
    private ScreenTransition screenTransition;

    /**
     * Change screen without transition
     */
    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null);
    }

    /**
     * Change screen with transition
     */
    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (!init) {
            currentFBO = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            nextFBO = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            batch = new SpriteBatch();
            init = true;
        }

        // Start new transition
        nextScreen = screen;
        nextScreen.show(); // Activate next screen
        nextScreen.resize(width, height);
        nextScreen.render(0); // let screen update once
        if (currentScreen != null) currentScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); // disable input
        this.screenTransition = screenTransition;
        t = 0;
    }

    @Override
    public void render() {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currentScreen != null) currentScreen.render(deltaTime);
        } else {
            // ongoing transition
            float duration = 0;
            if (screenTransition != null)
                duration = screenTransition.getDuration();
            // update progress of ongoing transition
            t = Math.min(t + deltaTime, duration);

            if (screenTransition == null || t >= duration) {
                // no transition effect set or transition just finished
                if (currentScreen != null) currentScreen.hide();
                nextScreen.resume();
                // enable input from next screen
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                // switch screen
                currentScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screen to FBOs
                currentFBO.begin();
                if (currentScreen != null) currentScreen.render(deltaTime);
                currentFBO.end();
                nextFBO.begin();
                nextScreen.render(deltaTime);
                nextFBO.end();

                // render transition effect to screen
                float alpha = t / duration;
                screenTransition.render(batch, currentFBO.getColorBufferTexture(), nextFBO.getColorBufferTexture(), alpha);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) currentScreen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);
    }

    @Override
    public void pause() {
        if (currentScreen != null) currentScreen.pause();
    }

    @Override
    public void resume() {
        if (currentScreen != null) currentScreen.resume();
    }

    @Override
    public void dispose() {
        if (currentScreen != null) currentScreen.hide();
        if (nextScreen != null) nextScreen.hide();
        if (init) {
            currentFBO.dispose();
            currentScreen = null;
            nextFBO.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

}
