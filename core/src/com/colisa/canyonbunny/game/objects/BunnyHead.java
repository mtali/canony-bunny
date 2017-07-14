package com.colisa.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.Enums.JUMP_STATE;
import com.colisa.canyonbunny.util.Enums.VIEW_DIRECTION;

@SuppressWarnings("WeakerAccess")
public class BunnyHead extends AbstractGameObject {
    private static final String TAG = BunnyHead.class.getName();
    public VIEW_DIRECTION viewDirection;
    public JUMP_STATE jumpState;
    public float timeJumping;
    public boolean hasFeatherPowerUp;
    public float timeLeftFeatherPowerUp;
    private TextureRegion region;

    public BunnyHead() {
        init();
    }

    public void init() {
        dimension.set(1, 1);
        region = Assets.instance.bunnyAssets.head;
        origin.set(dimension.x / 2, dimension.y / 2);

        viewDirection = VIEW_DIRECTION.RIGHT;
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        hasFeatherPowerUp = false;
        timeLeftFeatherPowerUp = 0;

        // Each object has to initialize its physical attributes which are :-
        // velocity, terminalVelocity, friction, acceleration and bounds
        velocity.set(0, 0);
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        bounds.set(0, 0, dimension.x, dimension.y);
    }

    public void setJumping(boolean jumpKeyPressed) {
        switch (jumpState) {
            case GROUNDED:
                if (jumpKeyPressed) {
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING:
                if (!jumpKeyPressed)
                    jumpState = JUMP_STATE.FALLING;
                break;
            case FALLING:
            case JUMP_FALLING:
                if (jumpKeyPressed && hasFeatherPowerUp) {
                    timeJumping = Constants.JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerUp(boolean picked) {
        hasFeatherPowerUp = picked;
        if (picked) {
            timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerUp() {
        return hasFeatherPowerUp && timeLeftFeatherPowerUp > 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (velocity.x != 0) {      // meaning the bunny head is moving
            viewDirection = velocity.x > 0 ? VIEW_DIRECTION.RIGHT : VIEW_DIRECTION.LEFT;
        }

        // Update timeLeftFeatherPowerUp
        if (hasFeatherPowerUp()) {
            timeLeftFeatherPowerUp -= deltaTime;
            if (timeLeftFeatherPowerUp < 0) {
                timeLeftFeatherPowerUp = 0;
                setFeatherPowerUp(false);
            }
        }
    }

    /**
     * WHY are we overriding this method
     * 1. Update the jumping state
     * 2. Update velocity
     * 3. Update jump time on each frame
     */
    @Override
    protected void updateMotionY(float deltaTime) {
        switch (jumpState) {
            case GROUNDED:
                jumpState = JUMP_STATE.FALLING;
                break;

            case JUMP_RISING:
                timeJumping += deltaTime;
                if (timeJumping < Constants.JUMP_TIME_MAX) {
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
                break;

            case FALLING:
                break;

            case JUMP_FALLING:
                timeJumping += deltaTime;
                if (timeJumping > 0 && timeJumping <= Constants.JUMP_TIME_MIN) {
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
        }
        if (jumpState != JUMP_STATE.GROUNDED)
            super.updateMotionY(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (hasFeatherPowerUp)
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);

        batch.draw(
                region.getTexture(),
                position.x,
                position.y,
                origin.x,
                origin.y,
                dimension.x,
                dimension.y,
                scale.x,
                scale.y,
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                viewDirection == VIEW_DIRECTION.LEFT,
                false
        );
        // reset color to white
        batch.setColor(1, 1, 1, 1);
    }

}
