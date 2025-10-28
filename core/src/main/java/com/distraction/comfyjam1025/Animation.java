package com.distraction.comfyjam1025;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    private TextureRegion[] images;
    private float interval;

    private float timer;
    public int frame;
    public int finishCount;

    public Animation() {

    }

    public Animation(TextureRegion[] images, float interval) {
        setAnimation(images, interval);
    }

    public void setAnimation(TextureRegion[] images, float interval) {
        this.images = images;
        this.interval = interval;
        frame = 0;
        timer = 0;
        finishCount = 0;
    }

    public int getFinishCount() {
        return finishCount;
    }

    public void update(float dt) {
        if (images == null) return;
        if (interval <= 0) return;
        timer += dt;
        if (timer >= interval) {
            timer -= interval;
            frame++;
            if (frame >= images.length) {
                frame = 0;
                finishCount++;
            }
        }
    }

    public TextureRegion getImage() {
        if (images == null) return null;
        return images[frame];
    }

}
