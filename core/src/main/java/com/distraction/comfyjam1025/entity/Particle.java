package com.distraction.comfyjam1025.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.Animation;
import com.distraction.comfyjam1025.Utils;

public class Particle extends Entity {

    protected final Animation animation;

    protected float time;

    public Particle(TextureRegion image) {
        this(new TextureRegion[] { image });
    }

    public Particle(TextureRegion[] images) {
        animation = new Animation(images, 0.016f);
        w = images[0].getRegionWidth();
        h = images[0].getRegionHeight();
    }

    @Override
    public void update(float dt) {
        animation.update(dt);
        if (animation.getFinishCount() > 0) remove = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, 1);
        Utils.drawCentered(sb, animation.getImage(), x, y);
    }
}
