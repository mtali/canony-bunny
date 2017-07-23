package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.colisa.canyonbunny.game.objects.BunnyHead;
import com.colisa.canyonbunny.game.objects.Carrot;
import com.colisa.canyonbunny.game.objects.Feather;
import com.colisa.canyonbunny.game.objects.GoldIcon;
import com.colisa.canyonbunny.game.objects.Rock;
import com.colisa.canyonbunny.screens.DirectedGame;
import com.colisa.canyonbunny.screens.MenuScreen;
import com.colisa.canyonbunny.screens.transitions.ScreenTransition;
import com.colisa.canyonbunny.screens.transitions.ScreenTransitionSlide;
import com.colisa.canyonbunny.util.AudioManager;
import com.colisa.canyonbunny.util.CameraHelper;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.Enums;

@SuppressWarnings("WeakerAccess")
public class WorldController extends InputAdapter implements Disposable {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;

    public Level level;
    public int lives;
    public int score;
    public float timeLeftGameOver;
    public float livesVisual;
    public float scoreVirtual;
    public World b2World;
    private DirectedGame game;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private BunnyHead bunnyHead;
    private boolean goalReached;

    // Accelerometer
    private boolean accelerometerAvailable;


    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void initPhysics() {
        if (b2World != null) b2World.dispose();
        b2World = new World(new Vector2(0, -9.81f), true);
        // Rocks
        Vector2 origin = new Vector2();
        for (Rock rock : level.rocks) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            Body body = b2World.createBody(bodyDef);
            rock.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }

    private void initLevel() {
        score = 0;
        scoreVirtual = score;
        level = new Level(Constants.LEVEL_1);
        initPhysics();
        cameraHelper.setTarget(level.bunnyHead);
        bunnyHead = level.bunnyHead;
    }

    private void init() {
        accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOver = 0;
        initLevel();
    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        if (isGameOver() || goalReached) {
            timeLeftGameOver -= deltaTime;
            if (timeLeftGameOver < 0) {
                backToMenu();
            }
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollision();
        b2World.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);

        if (!isGameOver() && isPlayerInWater()) {
            AudioManager.instance.play(Assets.instance.assetSounds.liveLost);
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

        if (scoreVirtual < score) {
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

        // Text collision Bunny Head <-> Goal
        if (!goalReached) {
            r2.set(level.goal.bounds);
            r2.x += level.goal.position.x;
            r2.y += level.goal.position.y;
            if (r1.overlaps(r2)) onCollideBunnyWithGoal();
        }
    }

    private void onCollisionBunnyWithFeather(Feather feather) {
        feather.collected = true;
        AudioManager.instance.play(Assets.instance.assetSounds.pickupFeather);
        score += feather.getScore();
        bunnyHead.setFeatherPowerUp(true);
    }

    private void onCollisionBunnyWithGoldIcon(GoldIcon goldIcon) {
        goldIcon.collected = true;
        AudioManager.instance.play(Assets.instance.assetSounds.pickupCoin);
        score += goldIcon.getScore();
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
                if (accelerometerAvailable){
                    // normalize accelerometer values from [-10,10] to [-1,1]
                    // which translate to rotation [-90,90] degrees
                    float amount = Gdx.input.getAccelerometerY() / 10.0f;
                    amount *= 90.0f;
                    // is angle of rotation inside dead zone?
                    if (Math.abs(amount) < Constants.ACCEL_ANGLE_DEAD_ZONE) {
                        amount = 0;
                    } else {
                        // use the defined angle of rotation insead of full 90degrees for max velocity
                        amount /= Constants.ACCEL_MAX_ANGLE_MOVEMENT;
                    }
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x * amount;
                } else if (Gdx.app.getType() != Application.ApplicationType.Desktop){
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
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
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }

    private void spawnCarrots(Vector2 pos, int numCarrots, float radius) {
        float carrotShapeScale = 0.5f;
        // Create carrots with box2d body and fixtures
        for (int i = 0; i < numCarrots; i++) {
            Carrot carrot = new Carrot();
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
            float carrotScale = MathUtils.random(0.5f, 1.5f);

            carrot.scale.set(carrotScale, carrotScale);

            // create box2d body for carrot with start position and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;

            Body body = b2World.createBody(bodyDef);
            body.setType(BodyDef.BodyType.DynamicBody);
            carrot.body = body;

            // create rectangular shape for carrot to allow
            // interactions / collisions with other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
            float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);

            // set physics attributes
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            level.carrots.add(carrot);

        }
    }

    private void onCollideBunnyWithGoal() {
        goalReached = true;
        timeLeftGameOver = Constants.TIME_DELAY_GAME_OVER;
        Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
        centerPosBunnyHead.x += level.bunnyHead.bounds.width;
        spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX, Constants.CARROTS_SPAWN_RADIUS);
    }

    @Override
    public void dispose() {
        if (b2World != null) b2World.dispose();
    }
}
