package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class GoldIcon extends AbstractGameObject {

    public boolean collected;
    private TextureRegion regionGoldIcon;

    public GoldIcon(){
        init();
    }

    private void init(){
        dimension.set(0.5f, 0.5f);
        regionGoldIcon = Assets.instance.goldCoinAssets.goldCoin;
        collected = false;
    }


    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion region = null;
        region = regionGoldIcon;
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

    public int getScore() {
        return 100;
    }
}
