package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractGameObject {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    /*
    * The following are variables represent all object physics attributes
    * They will be inherited by all game objects
    */

    /** Objects current speed in m/s. **/
    public Vector2 velocity;

    /** Objects positive and negative maximum velocity. **/
    public Vector2 terminalVelocity;

    /** Opposing force, slow down the object until its velocity equals zero**/
    public Vector2 friction;

    /** Objects constant acceleration in m/s2. **/
    public Vector2 acceleration;

    /**Object bounding box describe physical body for collision detection**/
    public Rectangle bounds;



    public AbstractGameObject() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;

        // Initializing object physics attributes
        velocity = new Vector2();
        terminalVelocity = new Vector2();
        friction = new Vector2();
        acceleration  = new Vector2();
        bounds = new Rectangle();
    }

    public abstract void render(SpriteBatch batch);
}
