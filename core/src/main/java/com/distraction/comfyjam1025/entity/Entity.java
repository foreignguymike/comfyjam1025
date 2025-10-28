package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract class Entity {

    public float x;
    public float y;
    public float dx;
    public float dy;
    public float w;
    public float h;
    public float rad;
    public float a;

    public boolean remove;

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
