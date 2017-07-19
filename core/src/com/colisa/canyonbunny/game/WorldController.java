package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.colisa.canyonbunny.game.objects.BunnyHead;
import com.colisa.canyonbunny.game.objects.Feather;
import com.colisa.canyonbunny.game.objects.GoldIcon;
import com.colisa.canyonbunny.game.objects.Rock;
import com.colisa.canyonbunny.screens.DirectedGame;
import com.colisa.canyonbunny.screens.MenuScreen;
import com.colisa.canyonbunny.util.CameraHelper;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.Enums;

@SuppressWarnings("WeakerAccess")
public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;

    public Level level;
    public int lives;
    public int score;
    public float timeLeftGameOver;
    public float livesVisual;
    public float scoreVirtual;
    private DirectedGame game;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private BunnyHead bunnyHead;


    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void initLevel() {
        score = 0;
        scoreVirtual = score;
        level = new Level(Constants.LEVEL_1);
        cameraHelper.setTarget(level.bunnyHead);
        bunnyHead = level.bunnyHead;
    }

    private void init() {
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOver = 0;
        initLevel();
    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        if (isGameOver()) {
            timeLeftGameOver -= deltaTime;
            if (timeLeftGameOver < 0) {
                return;
            }
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollision();
        cameraHelper.update(deltaTime);

        if (!isGameOver() && isPlayerInWater()) {
            lives--;
            if (isGameOver())
                timeLeftGameOver = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }

        level.mountains.updateScrollPosition(cameraHelper.getPosition());

        if (livesVisual > lives) {
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        }

        if (scoreVirtual < score){
            scoreVirtual = Math.min(score, scoreVirtual + 250 * deltaTime);
        }
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget(level.bunnyHead)) {
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
        } else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }

    private void testCollision() {
        // Update bunny frame with current position
        r1.set(bunnyHead.position.x, bunnyHead.position.y, bunnyHead.bounds.width, bunnyHead.bounds.height);

        // Test collision with rocks
        for (Rock r : level.rocks) {
            r2.set(r.position.x, r.position.y, r.bounds.width, r.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithRock(r);
        }

        // Test collision bunny head with gold coins
        for (GoldIcon gi : level.goldIcons) {
            if (gi.collected) continue;
            r2.set(gi.position.x, gi.position.y, gi.bounds.width, gi.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithGoldIcon(gi);
        }

        // Test collision bunny head with feather
        for (Feather feather : level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithFeather(feather);
        }
    }

    private void onCollisionBunnyWithFeather(Feather feather) {
        feather.collected = true;
        score += feather.getScore();
        bunnyHead.setFeatherPowerUp(true);
        Gdx.app.log(TAG, "Feather collected");
    }

    private void onCollisionBunnyWithGoldIcon(GoldIcon goldIcon) {
        goldIcon.collected = true;
        score += goldIcon.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }

    private void onCollisionBunnyWithRock(Rock rock) {
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            }
            return;
        }
        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jumpState = Enums.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void handleInputGame(float deltaTime) {
        if (cameraHelper.hasTarget(level.bunnyHead)) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                bunnyHead.velocity.x = -bunnyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                bunnyHead.velocity.x = bunnyHead.terminalVelocity.x;
            } else {
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    bunnyHead.velocity.x = bunnyHead.terminalVelocity.x;
                }
            }

            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                bunnyHead.setJumping(true);
            } else {
                bunnyHead.setJumping(false);
            }
        }
    }

    public boolean isGameOver() {
        return lives < 0;
    }

    public boolean isPlayerInWater() {
        return bunnyHead.position.y < -5;
    }

    public void backToMenu() {
        game.setScreen(new MenuScreen(game));
        Gdx.app.debug(TAG, "switched back to menu screen.");
    }
}
