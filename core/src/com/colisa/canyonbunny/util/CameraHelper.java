package com.colisa.canyonbunny.util;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.colisa.canyonbunny.game.objects.AbstractGameObject;


/**
 * Helper class stores the current position and zoom for the camera. It can also follow one game
 * object at a time when set as a target by calling setTarget(), target can be set to null to make
 * the camera stop following at all . To find out the last set target call getTarget(), checking for
 * null by hasTarget().
 * <p>
 * The update() method should be called on every update cycle to let it update the camera position
 * whatever needed
 * <p>
 * The applyTo() should be called at the beginning of rendering of new frame
 */
@SuppressWarnings("WeakerAccess")
public class CameraHelper {
    private static final String TAG = CameraHelper.class.getName();

    private Vector2 position;
    private float zoom;
    private AbstractGameObject target;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }

    public void update(float deltaTime) {
        // If camera doesn't have a target
        if (!hasTarget()) return;
        position.x = target.position.x + target.origin.x;
        position.y = target.position.y + target.origin.y;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, Constants.MAX_ZOOM_IN, Constants.MAX_ZOOM_OUT);
    }

    public AbstractGameObject getTarget() {
        return target;
    }

    public void setTarget(AbstractGameObject target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(AbstractGameObject target) {
        return hasTarget() && this.target.equals(target);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
