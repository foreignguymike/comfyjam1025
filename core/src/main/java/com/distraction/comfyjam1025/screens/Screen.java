package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;

public abstract class Screen {

    protected Context context;

    protected final TextureRegion pixel;

    public boolean transparent = false;

    protected final Vector3 m;
    protected OrthographicCamera cam;
    protected OrthographicCamera uiCam;
    protected OrthographicCamera textCam;

    protected SpriteBatch sb;

    protected boolean ignoreInput;

    public Transition in;
    public Transition out;

    protected Screen(Context context) {
        this.context = context;
        this.sb = context.sb;

        pixel = context.getPixel();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        uiCam = new OrthographicCamera();
        uiCam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        textCam = new OrthographicCamera();
        textCam.setToOrtho(false, Constants.WIDTH * 3, Constants.HEIGHT * 3);

        m = new Vector3();

        in = new Transition(context, Transition.Type.CHECKERED_IN, 0.5f, () -> ignoreInput = false);
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f);
    }

    public void unproject() {
        cam.unproject(m);
    }

    public void resume() {
    }

    public abstract void input();

    public abstract void update(float dt);

    public abstract void render();

}
