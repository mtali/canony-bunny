package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class BunnyHead extends AbstractGameObject {
    private static final String TAG = BunnyHead.class.getName();
    private TextureRegion region;

    public BunnyHead() {
        init();
    }

    public void init() {
        dimension.set(1, 1);
        region = Assets.instance.bunnyAssets.head;
        origin.set(dimension.x / 2, dimension.y / 2);
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
