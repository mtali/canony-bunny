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

    public void setLength(int length) {
        this.length = length;
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
