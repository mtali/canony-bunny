package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.util.Constants;

public class Rock extends AbstractGameObject {

    private TextureRegion regionEdge;
    private TextureRegion regionMiddle;

    // Floating effect
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;

    private int length;

    public Rock() {
        init();
    }

    private void init() {
        dimension.set(1, 1.5f);
        regionEdge = Assets.instance.rockAssets.edge;
        regionMiddle = Assets.instance.rockAssets.middle;
        // Start length of this rock
        setLength(1);

        floatingDownwards = false;
        floatCycleTimeLeft = MathUtils.random(0, Constants.FLOAT_CIRCLE_TIME);
        floatTargetPosition = null;
    }

    private void setLength(int length) {
        this.length = length;
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        floatCycleTimeLeft -= deltaTime;
        if (floatTargetPosition == null) {
            floatTargetPosition = new Vector2(position);
        }

        if (floatCycleTimeLeft <= 0) {
            floatCycleTimeLeft = Constants.FLOAT_CIRCLE_TIME;
            floatingDownwards = !floatingDownwards;
            body.setLinearVelocity(0, Constants.FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
        } else {
            body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = null;

        float relX = 0;
        float relY = 0;

        // Draw left edge
        region = regionEdge;
        relX -= dimension.x / 4;
        batch.draw(
                region.getTexture(),
                position.x + relX,
                position.y + relY,
                origin.x,
                origin.y,
                dimension.x / 4,
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

        // Draw middle
        relX = 0;
        region = regionMiddle;
        for (int i = 0; i < length; i++) {
            batch.draw(
                    region.getTexture(),
                    position.x + relX,
                    position.y + relY,
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
            relX += dimension.x;
        }

        // Draw right edge
        region = regionEdge;
        batch.draw(
                region.getTexture(),
                position.x + relX,
                position.y + relY,
                relX + dimension.x / 8,
                origin.y,
                dimension.x / 4,
                dimension.y,
                scale.x,
                scale.y,
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                true,
                false
        );

    }
}
