package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.TextData;
import com.distraction.comfyjam1025.entity.ImageEntity;
import com.distraction.comfyjam1025.entity.Leaf;
import com.distraction.comfyjam1025.entity.TextEntity;

import java.util.ArrayList;
import java.util.List;

public class GraveScene extends Screen {

    private static final TextData[][] TEXT_DATA = new TextData[][]{
        {
            new TextData("", 2f),
            new TextData("Again?\nThe same girl came back.", 5f),
            new TextData("I still don't know\nwhat she's doing here.", 5f),
            new TextData("I thought nobody cared.", 5f)
        }
    };

    private enum Action {
        GIRL_ENTER,
        GIRL_GIFT,
        GIRL_LEAVE,
        FADE_OUT
    }

    private Action action = Action.GIRL_ENTER;

    private final TextureRegion grass1;
    private final TextureRegion grass2;
    private final TextureRegion grass3;
    private final TextureRegion treeBg;
    private final TextureRegion tree;
    private final TextureRegion grave;

    private final TextEntity text;
    private float textTime;
    private int textIndex;
    private final TextData[] texts;

    private final ImageEntity girl;
    private final TextureRegion[] girlImages;

    private final float gravex = Constants.WIDTH / 2f - 15;

    private final List<ImageEntity> particles;
    private float leafTime;

    public GraveScene(Context context, int year) {
        super(context);

        in = new Transition(context, Transition.Type.FLASH_IN, 2f);
        in.setFlashColor(Color.BLACK);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 2f);
        out.setCallback(() -> context.sm.replace(new PlayScreen(context, year)));
        out.setFlashColor(Color.BLACK);

        grass1 = context.getImage("grass1");
        grass2 = context.getImage("grass2");
        grass3 = context.getImage("grass3");
        treeBg = context.getImage("treebg1");
        tree = context.getImage("tree");
        grave = context.getImage("grave");

        girlImages = context.getImage("girl").split(22, 55)[year - 1];
        girl = new ImageEntity(context, girlImages, 0.5f);

        texts = TEXT_DATA[year - 2];
        textTime = texts[0].duration;

        text = new TextEntity(context, context.getFont(Context.M5X716), "", 20, 110);
        text.setColor(1, 1, 1, 0);
        text.ta = 0;

        particles = new ArrayList<>();
    }

    @Override
    public void input() {
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        int girlFrame = girl.animation.frame;

        leafTime -= dt;
        if (leafTime <= 0) {
            leafTime = MathUtils.random(0.3f, 1.4f);
            float x = MathUtils.random(gravex + 25, gravex + 25 + 65);
            float y = MathUtils.random(97, 97 + 85);
            particles.add(new Leaf(context, x, y, -1f * MathUtils.random(1, 3), -1f * MathUtils.random(3, 6)));
        }

        if (action == Action.GIRL_ENTER) {
            textTime -= dt;
            if (textTime <= 0) {
                textIndex++;
                if (textIndex < texts.length) {
                    textTime = texts[textIndex].duration;
                    text.setText(texts[textIndex].text);
                    text.ta = 1f;
                }
            } else if (textTime <= 0.5f) {
                text.ta = 0f;
            }
        } else if (action == Action.GIRL_GIFT) {

        } else if (action == Action.GIRL_LEAVE) {

        } else if (action == Action.FADE_OUT) {

        }

        text.update(dt);
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(cam.combined);

        sb.setColor(Constants.SCENE_1_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Constants.SCENE_1_TREE_BG);
        sb.draw(pixel, 0, 90, Constants.WIDTH, 90);

        sb.setColor(1, 1, 1, 1);
        sb.draw(treeBg, 0, 0);
        sb.draw(grave, gravex, 8);
        sb.draw(tree, gravex, 9);

        sb.setColor(Constants.SCENE_1_GRASS);
        sb.draw(pixel, 0, 0, Constants.WIDTH, 8);

        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < 10; i++) {
            sb.draw(grass1, grass1.getRegionWidth() * i, 0);
            sb.draw(grass2, grass2.getRegionWidth() * i, 9);
            sb.draw(grass3, grass3.getRegionWidth() * i, 8);
        }

        text.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();
    }

}
