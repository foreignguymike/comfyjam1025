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
import java.util.Arrays;
import java.util.List;

public class GraveScene extends Screen {

    private static final float GIRL_SPEED = 30;
    private static final float GIRL_START = -20;

    private static final TextData[][] TEXT_DATA = new TextData[][]{
        {
            new TextData("", 2f),
            new TextData("Again?\nThe same girl came back.", 5f),
            new TextData("I still don't know\nwhat she's doing here.", 5f),
            new TextData("I thought nobody cared.", 5f)
        },
        {
            new TextData("", 2f),
            new TextData("A new gift.\nIt's a letter.", 5f),
            new TextData("I hear a soft whisper.", 5f),
            new TextData("\"Thank you\"", 5f)
        },
        {
            new TextData("", 4f),
            new TextData("In her hands,\nsomething small...", 5f),
            new TextData("fragile and\nstitched with care.", 5f)
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

        TextureRegion girlImage = context.getImage("girl");
        girlImages = girlImage.split(girlImage.getRegionWidth() / 4, girlImage.getRegionHeight() / 4)[year - 1];
        girl = new ImageEntity(context, Arrays.copyOfRange(girlImages, 0, 2), 0.5f);
        girl.x = GIRL_START;
        girl.y = 35;
        girl.setSize(22, 55);

        texts = TEXT_DATA[year - 2];
        textTime = texts[0].duration;

        text = new TextEntity(context, context.getFont(Context.M5X716), "", 20, 100);
        text.setColor(1, 1, 1, 0);
        text.ta = 0;
        text.vAlignment = TextEntity.VAlignment.BOTTOM;

        particles = new ArrayList<>();
        for (int i = 30; i >= 0; i--) {
            leafTime = MathUtils.random(0.3f, 1.4f);
            float x = MathUtils.random(gravex + 25, gravex + 25 + 65);
            float y = MathUtils.random(97, 97 + 85);
            particles.add(new Leaf(context, context.getImage("leaves"), x, y, -1f * MathUtils.random(1, 3), -1f * MathUtils.random(3, 6)));
            for (ImageEntity p : particles) p.update(leafTime);
        }
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
            particles.add(new Leaf(context, context.getImage("leaves"), x, y, -1f * MathUtils.random(1, 3), -1f * MathUtils.random(3, 6)));
        }

        textTime -= dt;
        if (textTime <= 0) {
            if (textIndex < texts.length - 1) {
                textIndex++;
                textTime = texts[textIndex].duration;
                text.setText(texts[textIndex].text);
                text.ta = 1f;
            }
        } else if (textTime <= 0.5f) {
            text.ta = 0f;
        }

        if (action == Action.GIRL_ENTER) {
            girl.update(dt);
            girl.x += GIRL_SPEED * dt;
            if (girl.x >= gravex + 15) {
                girl.x = gravex + 15;
                action = Action.GIRL_GIFT;
                girl.animation.setAnimation(Arrays.copyOfRange(girlImages, 2, 4), 1.5f);
            }
            if (girlFrame == 0 && girl.animation.frame != girlFrame) {
                float vol = MathUtils.clamp(0.2f * girl.x / (Constants.WIDTH / 2f), 0, 0.2f);
                context.audio.playSound("step", vol);
            }
        } else if (action == Action.GIRL_GIFT) {
            girl.update(dt);
            if (girl.animation.finishCount == 1 && girl.animation.frame == 1) {
                action = Action.GIRL_LEAVE;
                girl.hflip = true;
                girl.animation.setAnimation(Arrays.copyOfRange(girlImages, 0, 2), 0.5f);
            }
        } else if (action == Action.GIRL_LEAVE) {
            girl.update(dt);
            girl.x -= GIRL_SPEED * dt;
            if (girl.x < GIRL_START && textIndex >= texts.length - 1) {
                action = Action.FADE_OUT;
                out.start();
            }
            if (girlFrame == 0 && girl.animation.frame != girlFrame && girl.x > 0) {
                float vol = MathUtils.clamp(0.2f * girl.x / (Constants.WIDTH / 2f), 0, 0.2f);
                context.audio.playSound("step", vol);
            }
        }

        text.update(dt);

        for (int i = particles.size() - 1; i >= 0; i--) {
            ImageEntity p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(p);
        }
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
        sb.draw(treeBg, 0, -2, 320.1f, 95f);
        sb.draw(grave, gravex, 8, 30, 41);
        sb.draw(tree, gravex, 9, 126, 171);
        girl.render(sb);
        for (ImageEntity p : particles) p.render(sb);

        sb.setColor(Constants.SCENE_1_GRASS);
        sb.draw(pixel, 0, 0, Constants.WIDTH, 8);

        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < 10; i++) {
            sb.draw(grass1, 32 * i, 0, 32, 16);
            sb.draw(grass2, 38 * i, 9, 38, 5);
            sb.draw(grass3, 61 * i, 8, 61, 6);
        }

        text.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();
    }

}
