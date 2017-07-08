package com.colisa.canyonbunny;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class WorldController {
    private static final String TAG = WorldController.class.getName();
    public Sprite[] testSprites;
    private int selectedSprite;

    public WorldController(){
        init();
    }

    private void init(){
        initTestObjects();
    }

    private void initTestObjects(){
        // Create new array of 5 sprites
        testSprites = new Sprite[5];

        // Create empty POT-sized Pixmap with 8 bit RGBA pixel data
        int width = 32;
        int height = 32;
        Pixmap pixmap = createProceduralPixmap(width, height);

        // Create new texture from pixmap data
        Texture texture = new Texture(pixmap);

        // Create new sprites using just created texture
        for (int i = 0; i < testSprites.length; i++){
            Sprite spr = new Sprite(texture);
            // Define sprite size to be 1m * 1m in game world
            spr.setSize(1,1);
            // Set origin to sprite's center
            spr.setOrigin(spr.getWidth()/2, spr.getHeight()/2);
            // Calculate random position for sprite
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(randomX, randomY);
            // Put sprite into array
            testSprites[i] = spr;
        }

        // Set the first sprite as selected one
        selectedSprite = 0;

    }

    private Pixmap createProceduralPixmap(int width, int height){
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Fill square with red color at 50% opacity
        pixmap.setColor(1,0,0,0.5f);
        pixmap.fill();
        pixmap.setColor(1,1,0,1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        // Set a cyan-coloured border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

    public void update(float deltaTime){
        updateTextObjects(deltaTime);
    }

    private void updateTextObjects(float deltaTime){
        // Get current rotation fro selected sprite
        float rotation = testSprites[selectedSprite].getRotation();
        // Rotate sprite by 90 degrees per second
        rotation += 90 * deltaTime;
        // Wrap around at 360 degrees
        rotation %= 360;
        // Set rotation value to selected sprite
        testSprites[selectedSprite].setRotation(rotation);
    }
}
