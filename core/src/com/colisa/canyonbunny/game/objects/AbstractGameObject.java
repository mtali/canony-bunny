package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractGameObject {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    /**
     * Objects current speed in m/s.
     **/
    public Vector2 velocity;

    /**
     * Objects positive and negative maximum velocity.
     **/
    public Vector2 terminalVelocity;

    /**
     * Opposing force, slow down the object until its velocity equals zero
     **/
    public Vector2 friction;

    /**
     * Objects constant acceleration in m/s2.
     **/
    public Vector2 acceleration;

    /**
     * Object bounding box describe physical body for collision detection
     **/
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
        acceleration = new Vector2();
        bounds = new Rectangle();
    }


    protected void updateMotionX(float deltaTime) {
        if (velocity.x != 0) {
            // Apply friction
            if (velocity.x > 0) {
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
            }
            // Apply acceleration
            velocity.x += acceleration.x * deltaTime;
            // Make sure it doesn't exceed positive or negative terminal velocity
            velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
        }
    }

    protected void updateMotionY(float deltaTime) {
        if (velocity.y != 0) {
            // Apply friction
            if (velocity.y > 0) {
                velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.y += acceleration.y * deltaTime;
        // Make sure object velocity does not exceed positive and negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
    }

    public void update(float deltaTime) {
        // Update body X and Y velocity
        updateMotionX(deltaTime);
        updateMotionY(deltaTime);

        // Move object to new position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    public abstract void render(SpriteBatch batch);
}
