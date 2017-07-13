package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.colisa.canyonbunny.util.CameraHelper;
import com.colisa.canyonbunny.util.Constants;

@SuppressWarnings("WeakerAccess")
public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;

    public Level level;
    public int lives;
    public int score;


    public WorldController() {
        init();
    }

    private void initLevel(){
        score = 0;
        level = new Level(Constants.LEVEL_1);
    }

    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        initLevel();
    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        level.update(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget()) {
            // Camera Control (move)
            float cameraMoveSpeed = 5 * deltaTime;
            float cameraMoveSpeedAccelerationFactor = 10;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                cameraMoveSpeed *= cameraMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-cameraMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(cameraMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, cameraMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -cameraMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
        }

        // Camera Control (zoom)
        float cameraZoomSpeed = 1 * deltaTime;
        float cameraZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            cameraZoomSpeed *= cameraZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(cameraZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-cameraZoomSpeed);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);

    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }



    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resettled");
        }
        return false;
    }


}
