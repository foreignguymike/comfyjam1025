package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;
import com.distraction.comfyjam1025.entity.ImageEntity;
import com.distraction.comfyjam1025.entity.Leaf;
import com.distraction.comfyjam1025.entity.TextEntity;

import java.util.List;

public class OutroScreen extends Screen {

    private static final float PAN_SPEED = 10;

    private static final String SCRIPT =
        "I see it now.\n" +
            "All this time, I thought I had faded.\n" +
            "But she remembers year after year,\n" +
            "quietly carrying my heart with her.\n" +
            "And that’s enough.";

    private final List<ImageEntity> particles;
    private float leafTime;

    private final TextureRegion puzzleBg;
    private final TextureRegion[] images;
    private final float[] alphas;
    private float time;

    private final TextEntity text;
    private final TextureRegion next;

    public OutroScreen(Context context, List<ImageEntity> particles) {
        super(context);
        this.particles = particles;

        in = new Transition(context, Transition.Type.FLASH_IN, 1f);
        in.setFlashColor(Color.WHITE);
        in.start();

        out = new Transition(context, Transition.Type.FLASH_OUT, 4f, () -> context.sm.replace(new IntroScene(context)));
        out.setFlashColor(Color.WHITE);

        images = new TextureRegion[]{
            context.getImage("ts0"),
            context.getImage("ts1"),
            context.getImage("ts2")
        };
        alphas = new float[3];
        alphas[0] = 1;

        puzzleBg = context.getImage("puzzlebg");

        text = new TextEntity(context, context.getFont(Context.CON26), SCRIPT, Constants.WIDTH / 2f + 80, Constants.HEIGHT / 2f + 120 / 2f - 10);
        text.setColor(1, 1, 1, 0);
        text.a = 0;
        text.ta = 0;
        text.globalScale = textCam.viewportWidth / cam.viewportWidth;

        next = context.getImage("next");

        context.audio.playMusic("remembered", 0.7f, false);
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (time > 14) {
                ignoreInput = true;
                out.start();
            }
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        time += dt;
        alphas[0] = MathUtils.clamp(6 - time, 0f, 1f);
        alphas[1] = MathUtils.clamp(time < 5 ? time - 4 : time > 8 ? 1 - (time - 8) : 1, 0f, 1f);
        alphas[2] = MathUtils.clamp(time - 8, 0f, 1f);

        leafTime -= dt;
        if (leafTime <= 0) {
            leafTime = MathUtils.random(0.3f, 1.4f);
            float x = MathUtils.random(20, Constants.WIDTH + 80);
            float y = Constants.HEIGHT + 10;
            particles.add(new Leaf(context, context.getImage("leaves"), x, y, -1f * MathUtils.random(1, 3), -1f * MathUtils.random(3, 6)));
        }

        for (int i = particles.size() - 1; i >= 0; i--) {
            ImageEntity p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(p);
        }

        cam.position.x = MathUtils.clamp(cam.position.x + PAN_SPEED * dt, Constants.WIDTH / 2f, Constants.WIDTH / 2f + 80);
        cam.update();
        if (time > 12) text.ta = 1;
        text.update(dt);
        textCam.position.x = cam.position.x * text.globalScale;
        textCam.update();
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.PUZZLE_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setProjectionMatrix(cam.combined);
        for (ImageEntity p : particles) p.render(sb);

        sb.setColor(1, 1, 1, 1);
        Utils.drawCentered(sb, puzzleBg, Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 126, 126);
        sb.setColor(Constants.PUZZLE_PIECE_BG);
        Utils.drawCentered(sb, pixel, Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 120, 120);
        for (int i = 0; i < images.length; i++) {
            sb.setColor(1, 1, 1, alphas[i]);
            Utils.drawCentered(sb, images[i], Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 120, 120);
        }

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(textCam.combined);
        text.render(sb);
        if (time > 14 && (time % 0.9f) < 0.45f) {
            sb.setProjectionMatrix(uiCam.combined);
            sb.draw(next, 300, 10, 8, 8);
        }

        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();
    }
}
