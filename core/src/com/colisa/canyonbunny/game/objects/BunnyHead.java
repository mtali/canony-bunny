package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.util.Enums.JUMP_STATE;
import com.colisa.canyonbunny.util.Enums.VIEW_DIRECTION;

@SuppressWarnings("WeakerAccess")
public class BunnyHead extends AbstractGameObject {
    private static final String TAG = BunnyHead.class.getName();
    private TextureRegion region;

    public VIEW_DIRECTION viewDirection;
    public JUMP_STATE jumpState;
    public float timeJumping;
    public boolean hasFeatherPowerUp;
    public float timeLeftFeatherPowerUp;

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

    @Override
    public void render(SpriteBatch batch) {

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
                false,
                false
        );
        // reset color to white
        batch.setColor(1, 1, 1, 1);
    }

}
