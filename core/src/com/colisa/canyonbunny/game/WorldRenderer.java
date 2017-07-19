package com.colisa.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.GamePreferences;


public class WorldRenderer implements Disposable {

    private final static String TAG = WorldRenderer.class.getName();
    private OrthographicCamera camera;
    // Camera to for viewing score, lives and game performance(FPS)
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private WorldController worldController;


    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();

        // initializing GUI camera
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();

    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);
    }

    private void renderWorld(SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }

    private void renderGuiScore(SpriteBatch batch) {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if (worldController.scoreVirtual < worldController.score) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDistance = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDistance;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDistance;
        }
        batch.draw(Assets.instance.goldCoinAssets.goldCoin,
                x,
                y,
                offsetX,
                offsetY,
                100,
                100,
                0.35f,
                -0.35f,
                0
        );
        Assets.instance.assetFonts.defaultBig.draw(
                batch,
                String.valueOf((int) worldController.scoreVirtual),
                x + 75,
                y + 37
        );
    }

    private void renderGuiExtraLive(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (worldController.lives <= i)
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(
                    Assets.instance.bunnyAssets.head,
                    x + i * 50,
                    y,
                    50,
                    50,
                    129,
                    100,
                    0.35f,
                    -0.35f,
                    0
            );
            batch.setColor(1, 1, 1, 1);
        }

        if (worldController.lives >= 0 && worldController.livesVisual > worldController.lives) {
            int i = worldController.lives;
            float alphaColor = Math.max(0, worldController.livesVisual - worldController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + worldController.lives - worldController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(
                    Assets.instance.bunnyAssets.head,
                    x + i * 50,
                    y,
                    50,
                    50,
                    120,
                    100,
                    alphaScale,
                    -alphaScale,
                    alphaRotate
            );
            batch.setColor(1, 1, 1, 1);
        }
    }

    private void renderGuiFpsCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.assetFonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more show green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // reset font color
    }

    private void renderGui(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        renderGuiScore(batch);
        renderGiuFeatherPowerUp(batch);
        renderGuiExtraLive(batch);
        if (GamePreferences.instance.showFPSCounter)
            renderGuiFpsCounter(batch);
        renderGuiGameOverMessage(batch);
        batch.end();
    }

    public void resize(int width, int height) {
        float ratio = (float) width / (float) height;

        // updating world camera aspects ratio
        camera.viewportWidth = Constants.VIEWPORT_HEIGHT * ratio;
        camera.update();

        // updating camera GUI aspect ratio
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT * ratio;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    private void renderGuiGameOverMessage(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;

        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.assetFonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, true);
            fontGameOver.setColor(1, 1, 1, 1);
        }

    }

    private void renderGiuFeatherPowerUp(SpriteBatch batch) {
        float x = -15;
        float y = 30;
        float timeLeftFeatherPowerUp = worldController.level.bunnyHead.timeLeftFeatherPowerUp;
        if (timeLeftFeatherPowerUp > 0) {
            // Start icon fade in/out if the lef power-up time is less than 4 seconds. Fade interval
            // is set to 5 changes per second
            if (timeLeftFeatherPowerUp < 4) {
                if (((int) (timeLeftFeatherPowerUp * 5) % 2) != 0) {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(
                    Assets.instance.featherAssets.feather,
                    x,
                    y,
                    50,
                    50,
                    100,
                    100,
                    0.35f,
                    -0.35f,
                    0
            );

            Assets.instance.assetFonts.defaultSmall.draw(
                    batch,
                    String.valueOf((int) timeLeftFeatherPowerUp),
                    x + 60,
                    y + 57
            );
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
