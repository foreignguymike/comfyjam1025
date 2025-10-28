package com.distraction.comfyjam1025.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;

public class ImageEntity extends Entity {

    protected float time;

    public boolean hflip;

    public ImageEntity(Context context, TextureRegion image) {
        this(context, new TextureRegion[]{image}, 0);
    }

    public ImageEntity(Context context, TextureRegion[] images, float interval) {
        super(context);
        animation.setAnimation(images, interval);
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
        Utils.drawCentered(sb, animation.getImage(), x, y, hflip);
    }
}
