package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.entity.Leaf;
import com.distraction.comfyjam1025.entity.Particle;

import java.util.ArrayList;
import java.util.List;

public class Scene1Screen extends Screen {

    private final TextureRegion pixel;
    private final TextureRegion grass1;
    private final TextureRegion grass2;
    private final TextureRegion grass3;
    private final TextureRegion treeBg;
    private final TextureRegion tree;
    private final TextureRegion grave;

    private final List<Particle> particles;

    private float leafTime;

    private final float gravex = 350;

    public Scene1Screen(Context context) {
        super(context);
        pixel = context.getPixel();
        grass1 = context.getImage("grass1");
        grass2 = context.getImage("grass2");
        grass3 = context.getImage("grass3");
        treeBg = context.getImage("treebg1");
        tree = context.getImage("tree");
        grave = context.getImage("grave");

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 4f, () -> ignoreInput = false);
        in.start();

        particles = new ArrayList<>();
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        m.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        unproject();

    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        leafTime -= dt;
        if (leafTime <= 0) {
            leafTime = MathUtils.random(0.3f, 1.4f);
            float x = MathUtils.random(gravex + 25, gravex + 25 + 65);
            float y = MathUtils.random(97, 97 + 85);
            particles.add(new Leaf(context, x, y));
        }

        cam.position.x += 5 * dt;
        if (cam.position.x > gravex) cam.position.x = gravex;
        cam.update();

        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(p);
        }
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);

        sb.setColor(Constants.SCENE_1_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Constants.SCENE_1_TREE_BG);
        sb.draw(pixel, 0, 90, Constants.WIDTH, 90);

        sb.setProjectionMatrix(cam.combined);

        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < 3; i++) {
            sb.draw(treeBg, treeBg.getRegionWidth() * i, 0);
        }

        sb.setColor(1, 1, 1, 1);
        sb.draw(grave, gravex, 8);
        sb.draw(tree, gravex, 8);
        for (Particle p : particles) p.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.SCENE_1_GRASS);
        sb.draw(pixel, 0, 0, Constants.WIDTH, 8);

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < 20; i++) {
            sb.draw(grass1, grass1.getRegionWidth() * i, 0);
            sb.draw(grass2, grass2.getRegionWidth() * i, 9);
            sb.draw(grass3, grass3.getRegionWidth() * i, 8);
        }

        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();
    }

}
