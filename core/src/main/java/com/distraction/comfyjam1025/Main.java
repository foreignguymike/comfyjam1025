package com.distraction.comfyjam1025;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private static final float TICK = 1f / 60f;

    private Context context;

    private float accum;

    @Override
    public void create() {
        context = new Context();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1, true);
        context.sm.input();
        accum += Gdx.graphics.getDeltaTime();
        while (accum > TICK) {
            accum -= TICK;
            context.sm.update(TICK);
        }
        context.sm.render();
    }

    @Override
    public void dispose() {
        context.dispose();
    }
}
