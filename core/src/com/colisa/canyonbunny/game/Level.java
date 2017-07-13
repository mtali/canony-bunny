package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.colisa.canyonbunny.game.objects.AbstractGameObject;
import com.colisa.canyonbunny.game.objects.BunnyHead;
import com.colisa.canyonbunny.game.objects.Clouds;
import com.colisa.canyonbunny.game.objects.Feather;
import com.colisa.canyonbunny.game.objects.GoldIcon;
import com.colisa.canyonbunny.game.objects.Mountains;
import com.colisa.canyonbunny.game.objects.Rock;
import com.colisa.canyonbunny.game.objects.WaterOverlay;

@SuppressWarnings("WeakerAccess")
public class Level {
    private static final String TAG = Level.class.getName();
    // objects
    public Array<Rock> rocks;
    // decoration
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;
    public BunnyHead bunnyHead;
    public Array<GoldIcon> goldIcons;
    public Array<Feather> feathers;

    public Level(String fileName) {
        init(fileName);
    }

    public void init(String fileName) {
        // player character
        bunnyHead = null;
        // objects
        rocks = new Array<Rock>();
        goldIcons = new Array<GoldIcon>();
        feathers = new Array<Feather>();

        // load image file that represent the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(fileName));
        // Scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // Height glows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // Get color of the current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find the matching color value to identify block type at x,y, point and create
                // corresponding game object f there is a match
                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {

                } else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                } else if (BLOCK_TYPE.PLAYER_SPAWN_POINT.sameColor(currentPixel)) {
                    obj = new BunnyHead();
                    offsetHeight = -3f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bunnyHead = (BunnyHead) obj;
                } else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    feathers.add((Feather) obj);
                } else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new GoldIcon();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goldIcons.add((GoldIcon) obj);
                } else {
                    // Unknown object / pixel color

                    int r = 0xff & (currentPixel >>> 24);   // red color channel
                    int g = 0xff & (currentPixel >>> 16);   // green color channel
                    int b = 0xff & (currentPixel >>> 8);    // blue color channel
                    int a = 0xff & currentPixel;            // alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY +
                            ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">"
                    );
                }
                lastPixel = currentPixel;
            }
        }

        // decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1.25f);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);

        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + fileName + "' loaded");
    }

    public void render(SpriteBatch batch) {

        mountains.render(batch);

        for (Rock rock : rocks)
            rock.render(batch);

        for (GoldIcon goldIcon : goldIcons)
            goldIcon.render(batch);

        for (Feather feather : feathers)
            feather.render(batch);

        bunnyHead.render(batch);

        waterOverlay.render(batch);

        clouds.render(batch);
    }

    public void update(float deltaTime) {
        bunnyHead.update(deltaTime);
    }


    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // Black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWN_POINT(255, 255, 255), // white
        ITEM_FEATHER(255, 0, 255), // purple
        ITEM_GOLD_COIN(255, 255, 0);

        private int color;

        BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }

    }
}
