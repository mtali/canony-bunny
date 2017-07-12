package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractGameObject {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    // Attributes for game object physics and collision detection
    public Vector2 velocity;            // object current speed
    public Vector2 terminalVelocity;    // positive and negative maximum speed in m/s
    public Vector2 friction;            // opposing force until object velocity is 0

    public Vector2 acceleration;        // object constant acceleration m/s2
    public Vector2 bounds;              // physical body that will be used for collision detection

    public AbstractGameObject(){
        position = new Vector2();
        dimension = new Vector2(1,1);
        origin = new Vector2();
        scale = new Vector2(1,1);
        rotation = 0;

        velocity = new Vector2();
        terminalVelocity = new Vector2(1,1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Vector2();
    }

    public void update(float deltaTime){

    }

    public abstract void render(SpriteBatch batch);
}
