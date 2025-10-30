package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.graphics.Color;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.entity.TextEntity;

public class YearScreen extends Screen {

    private final TextEntity text;
    private float time = 0f;

    public YearScreen(Context context, int year) {
        super(context);
        text = new TextEntity(context, context.getFont(Context.VCR20), "YEAR " + year, Constants.WIDTH / 2f, Constants.HEIGHT / 2f, TextEntity.HAlignment.CENTER);
        text.setColor(1, 1, 1, 1);

        in = new Transition(context, Transition.Type.FLASH_IN, 2f);
        in.setFlashColor(Color.BLACK);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 2f);
        out.setCallback(() -> {
            if (year == 1) context.sm.replace(new PlayScreen(context, year));
            else context.sm.replace(new GraveScene(context, year));
        });
        out.setFlashColor(Color.BLACK);
    }

    @Override
    public void input() {

    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        time += dt;
        if (time >= 2 && !out.started()) {
            out.start();
        }
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(cam.combined);

        sb.setColor(0, 0, 0, 1);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(1, 1, 1, 1);
        text.render(sb);

        in.render(sb);
        out.render(sb);

        sb.end();
    }
}
