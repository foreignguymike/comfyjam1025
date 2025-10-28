package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.SimpleCallback;

public class Transition {

    public enum Type {
        CHECKERED_IN,
        CHECKERED_OUT,
        FLASH_IN,
        FLASH_OUT,
        PAN
    }

    private Type type;
    private final float duration;
    private SimpleCallback callback;

    private final Camera cam;
    private final Vector2 camStart;
    private final Vector2 camEnd;

    private final TextureRegion pixel;

    private boolean start;
    private float time;
    private boolean done;

    private Color flashColor = new Color(1, 1, 1, 1);

    public Transition(Context context, Type type, float duration) {
        this(context, type, null, null, null, duration, () -> {});
    }

    public Transition(Context context, Type type, float duration, SimpleCallback callback) {
        this(context, type, null, null, null, duration, callback);
    }

    public Transition(Context context, Type type, Camera cam, Vector2 camStart, Vector2 camEnd, float duration, SimpleCallback callback) {
        this.type = type;
        this.cam = cam;
        this.camStart = camStart;
        this.camEnd = camEnd;
        this.duration = duration;
        this.callback = callback;

        pixel = context.getPixel();
    }

    public void setFlashColor(Color color) {
        this.flashColor = color;
    }

    public void setTransition(Type type) {
        this.type = type;
    }

    public void setCallback(SimpleCallback callback) {
        this.callback = callback;
    }

    public void start() {
        reset();
        start = true;
    }

    public boolean started() {
        return start;
    }

    public boolean isFinished() {
        return done;
    }

    public void reset() {
        start = false;
        time = 0;
        done = false;
    }

    public float percent() {
        return time / duration;
    }

    private int snap(float f) {
        return MathUtils.round(f);
    }

    public void update(float dt) {
        if (!start || done) return;
        time += dt;
        if (type == Type.PAN) {
            cam.position.x = MathUtils.map(0, duration, camStart.x, camEnd.x, time);
            cam.position.y = MathUtils.map(0, duration, camStart.y, camEnd.y, time);
            cam.update();
        }
        if (time > duration) {
            if (!done) {
                done = true;
                callback.callback();
                if (type == Type.PAN) {
                    cam.position.x = camEnd.x;
                    cam.position.y = camEnd.y;
                    cam.update();
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!start || done) return;

        sb.setColor(Constants.BLACK);
        float squareSize = Constants.WIDTH / 16f;
        int numRows = MathUtils.ceil(Constants.HEIGHT / squareSize);
        int numCols = MathUtils.ceil(Constants.WIDTH / squareSize);
        if (type == Type.CHECKERED_IN) {
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    float size;
                    float ttime = time - ((numRows - row + col) / 40f) * (duration);
                    size = squareSize - squareSize * (ttime / (duration / 3));
                    size = MathUtils.clamp(size, 0, squareSize);
                    sb.draw(pixel, snap(squareSize * 0.5f + squareSize * col - size / 2), snap(squareSize * 0.5f + squareSize * row - size / 2), snap(size), snap(size));
                }
            }
        } else if (type == Type.CHECKERED_OUT) {
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    float size;
                    float ttime = time - ((numRows - row + col) / 40f) * (duration);
                    size = squareSize * (ttime / (duration / 3));
                    size = MathUtils.clamp(size, 0, squareSize);
                    sb.draw(pixel, snap(squareSize * 0.5f + squareSize * col - size / 2), snap(squareSize * 0.5f + squareSize * row - size / 2), snap(size), snap(size));
                }
            }
        } else if (type == Type.FLASH_IN) {
            sb.setColor(flashColor.r, flashColor.g, flashColor.b, 1f - (time / duration));
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        } else if (type == Type.FLASH_OUT) {
            sb.setColor(flashColor.r, flashColor.g, flashColor.b,time / duration);
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        }
    }

}
