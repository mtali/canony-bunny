package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class Carrot extends AbstractGameObject {
    private TextureRegion region;

    public Carrot() {
        init();
    }

    private void init() {
        dimension.set(0.25f, 0.5f);
        region = Assets.instance.levelDecorationAssets.carrot;
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(
                region.getTexture(),
                position.x - origin.x,
                position.y - origin.y,
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
    }
}
