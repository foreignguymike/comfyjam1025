package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.Utils;

public class ImageButton extends Entity {

    private final TextureRegion image;

    public ImageButton(TextureRegion image) {
        this(image, 0 , 0);
    }

    public ImageButton(TextureRegion image, float x, float y) {
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
