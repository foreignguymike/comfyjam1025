package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;

public class Leaf extends Particle {

    private static final float SPEED = 5;

    private float xo;
    private float yo;

    public Leaf(Context context, float x, float y) {
        super(context.getImage("leaves").split(5, 5)[0][MathUtils.random(0, 3)]);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        time += dt;
        a = MathUtils.clamp(time, 0, 1);

        x -= dt * SPEED / 5;
        y -= dt * SPEED;

        float sin = MathUtils.sin(time);
        xo = 6 * sin;
        yo = 3 * sin;

        rad = sin;

        if (y <= -10) remove = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, a);
        Utils.drawRotated(sb, animation.getImage(), x + xo, y + yo, rad);
    }
}
