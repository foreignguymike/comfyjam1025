package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;

public class ImageButton extends Entity {

    private final TextureRegion image;

    public ImageButton(Context context, TextureRegion image) {
        this(context, image, 0 , 0);
    }

    public ImageButton(Context context, TextureRegion image, float x, float y) {
        super(context);
        this.image = image;
        this.x = x;
        this.y = y;
        w = image.getRegionWidth();
        h = image.getRegionHeight();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, 1);
        Utils.drawCentered(sb, image, x, y);
    }
}
