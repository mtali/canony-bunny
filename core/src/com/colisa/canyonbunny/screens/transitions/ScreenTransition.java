package com.colisa.canyonbunny.screens.transitions;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ScreenTransition {

    /** Used to query duration of a transition effect*/
    float getDuration();

    /**Render the transition using two supplied textures with images of current and next screen
     * alpha value describe current state of the progress*
     * eg 0.0 will render from beginning, 0.25 will render from 25%*/
    void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha);
}
