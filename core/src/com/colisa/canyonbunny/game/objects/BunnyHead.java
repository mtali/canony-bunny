package com.colisa.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.util.AudioManager;
import com.colisa.canyonbunny.util.CharacterSkin;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.Enums.JUMP_STATE;
import com.colisa.canyonbunny.util.Enums.VIEW_DIRECTION;
import com.colisa.canyonbunny.util.GamePreferences;

@SuppressWarnings("WeakerAccess")
public class BunnyHead extends AbstractGameObject {
    private static final String TAG = BunnyHead.class.getName();
    public VIEW_DIRECTION viewDirection;
    public JUMP_STATE jumpState;
    public float timeJumping;
    public boolean hasFeatherPowerUp;
    public float timeLeftFeatherPowerUp;
    public ParticleEffect dustParticle = new ParticleEffect();

    // Animations
    private Animation animNormal;
    private Animation animCopterTransform;
    private Animation animCopterTransformBack;
    private Animation animCopterRotate;

    public BunnyHead() {
        init();
    }

    public void init() {
        dimension.set(1, 1);

        animNormal = Assets.instance.bunnyAssets.animNormal;
        animCopterTransform = Assets.instance.bunnyAssets.animCopterTransform;
        animCopterTransformBack = Assets.instance.bunnyAssets.animCopterTransformBack;
        animCopterRotate = Assets.instance.bunnyAssets.animCopterRotate;
        setAnimation(animNormal);


        origin.set(dimension.x / 2, dimension.y / 2);

        viewDirection = VIEW_DIRECTION.RIGHT;
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        hasFeatherPowerUp = false;
        timeLeftFeatherPowerUp = 0;

        // Particles
        dustParticle.load(Gdx.files.internal(Constants.DUST_PARTICLES), Gdx.files.internal("particles"));


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
                    AudioManager.instance.play(Assets.instance.assetSounds.jump);
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
                    AudioManager.instance.play(Assets.instance.assetSounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
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

        if (timeLeftFeatherPowerUp > 0) {
            if (animation == animCopterTransformBack) {
                // Restart "Transform" animation if another feather power up was picked
                // during "Transform Back" animation
                setAnimation(animCopterTransform);
            }

            timeLeftFeatherPowerUp -= deltaTime;

            if (timeLeftFeatherPowerUp < 0) {
                // disable power up
                timeLeftFeatherPowerUp = 0;
                setFeatherPowerUp(false);
                setAnimation(animCopterTransformBack);
            }
        }

        dustParticle.update(deltaTime);

        // Change animation state according to power up
        if (hasFeatherPowerUp){
            if (animation == animNormal) {
                setAnimation(animCopterTransform);
            } else if (animation == animCopterTransform){
                if (animation.isAnimationFinished(stateTime)){
                    setAnimation(animCopterRotate);
                }
            }
        } else {
            if (animation == animCopterRotate){
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(animCopterTransformBack);
            } else if(animation == animCopterTransformBack){
                if (animation.isAnimationFinished(stateTime)){
                    setAnimation(animNormal);
                }
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
                if (velocity.x != 0) {
                    dustParticle.setPosition(position.x + dimension.x / 2, position.y);
                    dustParticle.start();
                }
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
        if (jumpState != JUMP_STATE.GROUNDED) {
            dustParticle.allowCompletion();
            super.updateMotionY(deltaTime);
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        // Draw particle
        dustParticle.setPosition(position.x + origin.x, position.y);
        dustParticle.draw(batch);


        batch.setColor(CharacterSkin.values()[GamePreferences.instance.characterSkin].getColor());

        if (hasFeatherPowerUp)
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);

        TextureRegion region = null;
        region = (TextureRegion) animation.getKeyFrame(stateTime, true);
        float dimCorrectionX = 0;
        float dimCorrectionY = 0;
        if (animation != animNormal){
            dimCorrectionX = 0.05f;
            dimCorrectionY = 0.2f;
        }

        batch.draw(
                region.getTexture(),
                position.x,
                position.y,
                origin.x,
                origin.y,
                dimension.x + dimCorrectionX,
                dimension.y + dimCorrectionY,
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
