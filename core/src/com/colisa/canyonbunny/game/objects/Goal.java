package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class Goal extends AbstractGameObject {
    private TextureRegion region;

    public Goal(){
        init();
    }

    private void init(){
        dimension.set(3.0f, 3.0f);
        region = Assets.instance.levelDecorationAssets.goal;
        // It needs to have maximum height to ensure user always collide with it
        bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_EXPONENT);
        origin.set(dimension.x / 2.0f, 0.0f);
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
