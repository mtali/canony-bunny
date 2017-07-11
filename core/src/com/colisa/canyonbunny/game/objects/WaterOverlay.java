package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.colisa.canyonbunny.game.Assets;

public class WaterOverlay extends AbstractGameObject {
    private TextureRegion regionWaterOverlay;
    private float length;

    public WaterOverlay(float length){
        this.length = length;
        init();
    }

    private void init(){
        dimension.set(length * 3, 3);
        regionWaterOverlay = Assets.instance.levelDecorationAssets.waterOverlay;
        origin.x = -dimension.x / 2;
    }
    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = null;
        region = regionWaterOverlay;
        batch.draw(
                region.getTexture(),
                position.x + origin.x,
                position.y + origin.y,
                origin.x,
                origin.y,
                dimension.x,
                dimension.y,
                scale.x,
                scale.y,
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(), region.getRegionHeight(), false, false

        );
    }
}
