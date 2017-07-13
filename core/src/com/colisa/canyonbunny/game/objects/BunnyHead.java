package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.util.Constants;

public class BunnyHead extends AbstractGameObject {
    private static final String TAG = BunnyHead.class.getName();

    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeatherPowerUp;
    public  float timeLeftFeatherPowerUp;
    private TextureRegion regionHead;
    public BunnyHead(){
        init();
    }

    public void init(){
        dimension.set(1,1);
        regionHead = Assets.instance.bunnyAssets.head;
        origin.set(dimension.x / 2, dimension.y / 2);
        bounds.set(0, 0, dimension.x, dimension.y);

        // Physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        viewDirection = VIEW_DIRECTION.RIGHT;
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        hasFeatherPowerUp = false;
        timeLeftFeatherPowerUp = 0;
    }

    public void setJumpState(boolean jumpKeyPressed){
        switch (jumpState){
            case GROUNDED:                      // character is standing on a platform
                if (jumpKeyPressed){
                    timeJumping = 0;    // start counting time from the beginning
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;

            case JUMP_RISING:                   // rising in the air
                if (!jumpKeyPressed)
                    jumpState = JUMP_STATE.JUMP_FALLING;
                break;
            case FALLING:                       // falling down
            case JUMP_FALLING:                  // falling down after jump
                if (jumpKeyPressed && hasFeatherPowerUp){
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_FALLING;
                }
        }
    }

    public void setFeatherPowerUp(boolean pickedUp){
        hasFeatherPowerUp = pickedUp;
        if (pickedUp){
            timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerUp(){
        return hasFeatherPowerUp && timeLeftFeatherPowerUp > 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (velocity.x != 0){
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerUp > 0){
            timeLeftFeatherPowerUp -= deltaTime;
            if (timeLeftFeatherPowerUp < 0){
                // Disable power up
                timeLeftFeatherPowerUp = 0;
                setFeatherPowerUp(false);
            }
        }
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        switch (jumpState){
            case GROUNDED:
                jumpState = JUMP_STATE.FALLING;
                break;
            case JUMP_RISING:
                // Keep track of jump time
                timeJumping += deltaTime;
                // Jump time left
                if (timeJumping <= JUMP_TIME_MAX){
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
                // Add delta time to track jump time
                timeJumping += deltaTime;
                // Jump to minimal height if jump key was pressed too shot
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN){
                    velocity.y = terminalVelocity.y;
                }
        }

        if (jumpState != JUMP_STATE.GROUNDED)
            super.updateMotionY(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = null;

        // Special color when object has feather power up
        if (hasFeatherPowerUp)
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);

        region = regionHead;
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

    public enum VIEW_DIRECTION {
        LEFT, RIGHT
    }

    public enum JUMP_STATE {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }
}
