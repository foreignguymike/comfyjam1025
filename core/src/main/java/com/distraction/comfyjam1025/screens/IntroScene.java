package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class IntroScene extends Screen {

    private static final float PAN_SPEED = 4;
    private static final float GIRL_SPEED = 30;
    private static final float GIRL_START = 150;

    private static final TextData[] TEXT_DATA_1 = new TextData[]{
        new TextData("Another year. Another autumn.", 4),
        new TextData("I used to think someone might visit...", 4.5f),
        new TextData("But family is gone now. No friends left.", 4.5f),
        new TextData("No one to remember me.", 4),
        new TextData("It's strange, being forgotten.", 4),
        new TextData("You fade a little more each year,", 4.5f),
        new TextData("like trees losing their leaves.", 4.5f),
        new TextData("I suppose that's alright.", 4),
        new TextData("I've had a quiet life.", 4),
        new TextData("Maybe that's enough.", 4),
        new TextData("", 4),
        new TextData("Hmm? Someone's coming.", 4)
    };

    private static final TextData[] TEXT_DATA_2 = new TextData[]{
        new TextData("...Who was that?", 3),
        new TextData("She left something...", 3)
    };

    private enum Action {
        TITLE,
        INTRO_PAN,
        GIRL_ENTER,
        GIRL_GIFT,
        GIRL_LEAVE,
        FADE_OUT
    }

    private Action action = Action.TITLE;

    private final TextureRegion grass1;
    private final TextureRegion grass2;
    private final TextureRegion grass3;
    private final TextureRegion treeBg;
    private final TextureRegion tree;
    private final TextureRegion grave;

    private final List<ImageEntity> particles;

    private final TextureRegion[] girlImages;
    private final ImageEntity girl;

    private float leafTime;

    private final float gravex = 350;
    private final float panx = gravex + 10;

    private float textTime;
    private float nextTextTime = 4;
    private final TextEntity text;
    private int textIndex = -1;
    private TextData[] texts = TEXT_DATA_1;

    private final TextEntity titleText;
    private final TextEntity startText;

    public IntroScene(Context context) {
        super(context);

        text = new TextEntity(context, context.getFont(Context.CON26), "", 20, 100);
        text.ta = 0;
        text.globalScale = textCam.viewportWidth / cam.viewportWidth;

        grass1 = context.getImage("grass1");
        grass2 = context.getImage("grass2");
        grass3 = context.getImage("grass3");
        treeBg = context.getImage("treebg1");
        tree = context.getImage("tree");
        grave = context.getImage("grave");

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 2f, () -> ignoreInput = false);
        in.start();

        particles = new ArrayList<>();

        TextureRegion girlImage = context.getImage("girl");
        girlImages = girlImage.split(girlImage.getRegionWidth() / 4, girlImage.getRegionHeight() / 4)[0];
        girl = new ImageEntity(context, Arrays.copyOfRange(girlImages, 0, 2), 0.5f);
        girl.setSize(22, 55);
        girl.x = -200;
        girl.y = 35;

        titleText = new TextEntity(context, context.getFont(Context.PL56), Constants.TITLE, Constants.WIDTH / 2f, 130, TextEntity.HAlignment.CENTER, TextEntity.VAlignment.CENTER);
        titleText.a = titleText.ta = 0;
        titleText.globalScale = textCam.viewportWidth / cam.viewportWidth;
        startText = new TextEntity(context, context.getFont(Context.CON26), "Start", Constants.WIDTH / 2f, 40, TextEntity.HAlignment.CENTER, TextEntity.VAlignment.CENTER);
        startText.a = startText.ta = 0;
        startText.globalScale = textCam.viewportWidth / cam.viewportWidth;
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        if (action == Action.TITLE) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && textTime > 4) {
                titleText.ta = 0;
                startText.ta = 0;
                textTime = 0;
                action = Action.INTRO_PAN;
            }
        }
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

        titleText.update(dt);
        startText.update(dt);

        if (action == Action.TITLE) {
            textTime += dt;
            if (textTime > 2) {
                titleText.ta = 1;
            }
            if (textTime > 4) {
                startText.ta = 1;
            }
        } else if (action == Action.INTRO_PAN) {
            textTime += dt;
            if (textTime > 1f && !context.audio.isMusicPlaying()) {
                context.audio.playMusic("forgotten", 0.7f, false);
            }
            if (textTime > nextTextTime) {
                textIndex++;
                if (textIndex < texts.length) {
                    nextTextTime += texts[textIndex].duration;
                    text.setText(texts[textIndex].text);
                    text.ta = 1f;
                } else {
                    text.ta = 0f;
                    action = Action.GIRL_ENTER;
                    girl.x = GIRL_START;
                    textIndex = -1;
                    textTime = 0;
                    nextTextTime = 6;
                    texts = TEXT_DATA_2;
                }
            } else if (textTime > nextTextTime - 0.5f) {
                text.ta = 0f;
            }
            text.update(dt);

            cam.position.x += PAN_SPEED * dt;
            if (cam.position.x > panx) {
                cam.position.x = panx;
            }
            cam.update();
        } else if (action == Action.GIRL_ENTER) {
            girl.update(dt);
            girl.x += GIRL_SPEED * dt;
            if (girl.x >= gravex + 15) {
                girl.x = gravex + 15;
                action = Action.GIRL_GIFT;
                girl.animation.setAnimation(Arrays.copyOfRange(girlImages, 2, 4), 1.5f);
            }
            if (girlFrame == 0 && girl.animation.frame != girlFrame) {
                float vol = MathUtils.clamp(0.2f - 0.2f * (panx - girl.x) / (Constants.WIDTH / 2f), 0, 0.2f);
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
            if (girl.x < GIRL_START && textIndex == texts.length) {
                action = Action.FADE_OUT;
                out = new Transition(context, Transition.Type.FLASH_OUT, 4f);
                out.setCallback(() -> {
                    context.sm.replace(new YearScreen(context, 1));
                });
                out.setFlashColor(Color.BLACK);
                out.start();
            }
            textTime += dt;
            if (textTime > nextTextTime) {
                textIndex++;
                if (textIndex < texts.length) {
                    nextTextTime += texts[textIndex].duration;
                    text.setText(texts[textIndex].text);
                    text.ta = 1f;
                } else {
                    text.ta = 0f;
                }
            } else if (textTime > nextTextTime - 0.5f) {
                text.ta = 0f;
            }
            text.update(dt);
            if (girlFrame == 0 && girl.animation.frame != girlFrame && girl.x > panx - Constants.WIDTH / 2f) {
                float vol = MathUtils.clamp(0.2f - 0.2f * (panx - girl.x) / (Constants.WIDTH / 2f), 0, 0.2f);
                context.audio.playSound("step", vol);
            }
        }

        for (int i = particles.size() - 1; i >= 0; i--) {
            ImageEntity p = particles.get(i);
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
        for (int i = -1; i < 2; i++) {
            sb.draw(treeBg, 320f * i + cam.position.x / 2f, -2, 320.1f, 95f);
        }

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(textCam.combined);
        titleText.render(sb);
        startText.render(sb);
        sb.setProjectionMatrix(cam.combined);
        sb.draw(grave, gravex, 8, 30, 41);
        sb.draw(tree, gravex, 9, 126, 171);
        girl.render(sb);
        for (ImageEntity p : particles) p.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.SCENE_1_GRASS);
        sb.draw(pixel, 0, 0, Constants.WIDTH, 8);

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < 20; i++) {
            sb.draw(grass1, 32 * i, 0, 32, 16);
            sb.draw(grass2, 38 * i, 9, 38, 5);
            sb.draw(grass3, 61 * i, 8, 61, 6);
        }

        sb.setProjectionMatrix(textCam.combined);
        text.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();
    }

}
