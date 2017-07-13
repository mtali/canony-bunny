package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class Rock extends AbstractGameObject {

    private TextureRegion regionEdge;
    private TextureRegion regionMiddle;

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
    }

    private void setLength(int length) {
        this.length = length;
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
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
