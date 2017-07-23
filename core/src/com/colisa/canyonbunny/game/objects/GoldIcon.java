package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.colisa.canyonbunny.game.Assets;

public class GoldIcon extends AbstractGameObject {

    public boolean collected;
    private TextureRegion regionGoldIcon;

    public GoldIcon(){
        init();
    }

    private void init(){
        setAnimation(Assets.instance.goldCoinAssets.animGoldIcon);
        stateTime = MathUtils.random(0.0f, 1.0f);
        dimension.set(0.5f, 0.5f);
        regionGoldIcon = Assets.instance.goldCoinAssets.goldCoin;
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }


    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion region = null;
        region = (TextureRegion) animation.getKeyFrame(stateTime);
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
