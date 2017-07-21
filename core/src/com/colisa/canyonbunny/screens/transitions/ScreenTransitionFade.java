package com.colisa.canyonbunny.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionFade implements ScreenTransition {
    private static final ScreenTransitionFade instance = new ScreenTransitionFade();
    private float duration;

    public static ScreenTransitionFade init(float duration) {
        instance.duration = duration;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        alpha = Interpolation.fade.apply(alpha);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(
                currentScreen,
                0,
                0,
                0,
                0,
                width,
                height,
                1,
                1,
                0,
                0,
                0,
                currentScreen.getWidth(),
                currentScreen.getHeight(),
                false,
                true
        );

        batch.setColor(1, 1, 1, alpha);
        batch.draw(
                nextScreen,
                0,
                0,
                0,
                0,
                width,
                height,
                1,
                1,
                0,
                0,
                0,
                nextScreen.getWidth(),
                nextScreen.getHeight(),
                false,
                true

        );

        batch.end();
    }
}
