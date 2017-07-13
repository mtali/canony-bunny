package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class Feather extends AbstractGameObject {
    public boolean collected;
    private TextureRegion regionFeather;

    public  Feather(){
        init();
    }

    private void init(){
        dimension.set(0.5f, 0.5f);
        regionFeather = Assets.instance.featherAssets.feather;
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion region = null;
        region = regionFeather;
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
    }

    public int getScore(){
        return 250;
    }
}
