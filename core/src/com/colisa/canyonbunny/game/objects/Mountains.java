package com.colisa.canyonbunny.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.colisa.canyonbunny.game.Assets;

public  class Mountains extends AbstractGameObject {

    private TextureRegion regionMountainLeft;
    private TextureRegion regionMountainRight;

    private int length;

    public Mountains(int length){
        this.length = length;
        init();
    }

    private void init(){
        dimension.set(10, 2);
        regionMountainLeft = Assets.instance.levelDecorationAssets.mountainLeft;
        regionMountainRight = Assets.instance.levelDecorationAssets.mountainRight;

        // shift mountains and extend length
        origin.x = -dimension.x * 2;
        length += dimension.x * 2;
    }

    private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tinyColor, float parallaxSpeedX){
        TextureRegion region = null;
        batch.setColor(tinyColor, tinyColor, tinyColor, 1);
        float xRel = dimension.x * offsetX;
        float yRel = dimension.y * offsetY;

        // mountains span the whole level
        int mountainLength = 0;
        mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
        mountainLength += MathUtils.ceil(0.5f + offsetX);
        for (int i = 0; i < mountainLength; i++){
            // mountain left
            region = regionMountainLeft;
            batch.draw(
                    region.getTexture(),
                    origin.x + xRel + position.x * parallaxSpeedX,
                    origin.y + yRel + position.y,
                    origin.x,
                    origin.y,
                    dimension.x,
                    dimension.y,
                    scale.x,
                    scale.y,
                    rotation,
                    region.getRegionX(), region.getRegionY(),
                    region.getRegionWidth(), region.getRegionHeight(), false, false
            );
            xRel += dimension.x;
            region = regionMountainRight;
            batch.draw(
                    region.getTexture(),
                    origin.x + xRel + position.x * parallaxSpeedX,
                    position.y + origin.y + yRel,
                    origin.x,
                    origin.y,
                    dimension.x,
                    dimension.y,
                    scale.x,
                    scale.y,
                    rotation,
                    region.getRegionX(), region.getRegionY(),
                    region.getRegionWidth(), region.getRegionHeight(), false, false
            );
            xRel += dimension.x;
        }

        // reset color to white
        batch.setColor(1,1,1,1);
    }


    @Override
    public void render(SpriteBatch batch) {
        // 80% distant mountain (dark gray)
        drawMountain(batch, 0.5f, 0.5f, 0.5f, 0.8f);
        // 50% distant mountains (gray)
        drawMountain(batch, 0.25f, 0.25f, 0.7f, 0.5f);
        // 30% distant mountains (light gray)
        drawMountain(batch, 0.0f, 0.0f, 0.9f, 0.3f);
    }

    public void updateScrollPosition(Vector2 camPosition){
        position.set(camPosition.x, position.y);
    }
}
