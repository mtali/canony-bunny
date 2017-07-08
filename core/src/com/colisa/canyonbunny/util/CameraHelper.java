package com.colisa.canyonbunny.util;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Helper class stores the current position and zoom for the camera. It can also follow one game
 * object at a time when set as a target by calling setTarget(), target can be set to null to make
 * the camera stop following at all . To find out the last set target call getTarget(), checking for
 * null by hasTarget().
 *
 * The update() method should be called on every update cycle to let it update the camera position
 * whatever needed
 *
 * The applyTo() should be called at the beginning of rendering of new frame
 */
@SuppressWarnings("WeakerAccess")
public class CameraHelper {
    private static final String TAG = CameraHelper.class.getName();

    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10f;

    private Vector2 position;
    private float zoom;
    private Sprite target;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }

    public void update(float deltaTime) {
        if (!hasTarget()) return;
        position.x = target.getX() + target.getOriginX();
        position.y = target.getY() + target.getOriginY();
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

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom() {return zoom; }

    public void setTarget(Sprite target) { this.target = target; }
    public Sprite getTarget() { return target; }
    private boolean hasTarget() { return target != null; }
    public boolean hasTarget(Sprite sprite) {return  hasTarget() && this.target.equals(sprite); }

    public void applyTo (OrthographicCamera camera){
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
