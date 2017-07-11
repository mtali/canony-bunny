package com.colisa.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.colisa.canyonbunny.game.Assets;

@SuppressWarnings("WeakerAccess")
public class Clouds extends AbstractGameObject {

    private float length;

    private Array<TextureRegion> regionClouds;
    private Array<Cloud> clouds;

    public Clouds(float length) {
        this.length = length;
        init();
    }

    private void init() {
        dimension.set(3.0f, 1.5f);
        regionClouds = new Array<TextureRegion>();
        regionClouds.add(Assets.instance.levelDecorationAssets.cloud1);
        regionClouds.add(Assets.instance.levelDecorationAssets.cloud2);
        regionClouds.add(Assets.instance.levelDecorationAssets.cloud3);

        int distributionFactor = 5;
        int numberOfClouds = (int) (length / distributionFactor);
        clouds = new Array<Cloud>(2 * numberOfClouds);
        for (int i = 0; i < numberOfClouds; i++) {
            Cloud cloud = spawnCloud();
            cloud.position.x = i * distributionFactor;
            clouds.add(cloud);
        }
    }

    private Cloud spawnCloud() {
        Cloud cloud = new Cloud();
        cloud.dimension.set(dimension);
        cloud.setRegionCloud(regionClouds.random());

        // Position
        Vector2 pos = new Vector2();
        pos.x = length + 10;
        pos.y += 1.75;
        pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1);
        cloud.position.set(pos);
        return cloud;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Cloud cloud : clouds){
            cloud.render(batch);
        }
    }

    private class Cloud extends AbstractGameObject {
        private TextureRegion regionCloud;

        public Cloud() {
        }

        public void setRegionCloud(TextureRegion regionCloud) {
            this.regionCloud = regionCloud;
        }

        @Override
        public void render(SpriteBatch batch) {
            TextureRegion region = regionCloud;
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
                    region.getRegionWidth(),
                    region.getRegionHeight(), false, false
            );
        }
    }
}
