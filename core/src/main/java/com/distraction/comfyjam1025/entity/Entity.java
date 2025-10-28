package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.comfyjam1025.Animation;
import com.distraction.comfyjam1025.Context;

public class Entity {

    protected Context context;

    public float x;
    public float y;
    public float dx;
    public float dy;
    public float w;
    public float h;
    public float rad;
    public float a = 1;

    public final Animation animation;

    public boolean remove;

    public Entity(Context context) {
        this.context = context;
        animation = new Animation();
    }

    public boolean contains(float x, float y) {
        return contains(x, y, 0, 0);
    }

    public boolean contains(float x, float y, float px, float py) {
        return x > this.x - w / 2 - px
            && x < this.x + w / 2 + px
            && y > this.y - h / 2 - py
            && y < this.y + h / 2 + py;
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch sb) {

    }

}
