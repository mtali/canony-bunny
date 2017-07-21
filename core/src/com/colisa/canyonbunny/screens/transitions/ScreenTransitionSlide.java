package com.colisa.canyonbunny.screens.transitions;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionSlide implements ScreenTransition {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    private static final String TAG = ScreenTransitionSlide.class.getSimpleName();
    private static final ScreenTransitionSlide instance = new ScreenTransitionSlide();
    private float duration;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;
    private ScreenTransitionSlide() {
    }

    public static ScreenTransitionSlide init(float duration, int direction, boolean slideOut, Interpolation easing) {
        instance.duration = duration;
        instance.direction = direction;
        instance.slideOut = slideOut;
        instance.easing = easing;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha) {
        float width = currentScreen.getWidth();
        float height = currentScreen.getHeight();
        float x  = 0;
        float y = 0;
        if (easing != null) alpha = easing.apply(alpha);

        // calculate position offset
        switch (direction){
            case LEFT:
                x = -width * alpha;
                if (!slideOut) x += width;
                break;
            case RIGHT:
                x = width * alpha;
                if (!slideOut) x -= width;
                break;
            case UP:
                y = height * alpha;
                if (!slideOut) y -= -height;
                break;
            case DOWN:
                y = -height * alpha;
                if (!slideOut) y += height;
                break;
        }

        // drawing order depens on slide type ('in' or 'out')
        Texture textureBottom = slideOut ? nextScreen : currentScreen;
        Texture textureTop = slideOut ? currentScreen : nextScreen;

        // finally, draw both screens
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(
                textureBottom,
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
                currentScreen.getHeight(), false, true
        );

        batch.draw(
                textureTop,
                x,
                y,
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
                nextScreen.getHeight(), false, true
        );

        batch.end();
    }
}
