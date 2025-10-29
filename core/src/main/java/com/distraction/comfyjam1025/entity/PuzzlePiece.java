package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;

public class PuzzlePiece extends ImageEntity {

    private static final float[] ROTATIONS = {
        0,
        MathUtils.PI / 2f,
        MathUtils.PI,
        3 * MathUtils.PI / 2f
    };

    public int id;
    public int row;
    public int col;

    private float destx;
    private float desty;
    private float destrad;

    private float totalDist;
    private float jumpy;
    private float scale;

    public int rotateIndex = 0;

    public PuzzlePiece(Context context, int id, int row, int col, TextureRegion image) {
        super(context, image);
        this.id = id;
        setCell(row, col);
    }

    public void setCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setPosition(float x, float y) {
        this.x = this.destx = x;
        this.y = this.desty = y;
    }

    public void setDest(float destx, float desty) {
        if (!atDestination()) return;
        this.destx = destx;
        this.desty = desty;
        dx = destx - x;
        dy = desty - y;
        totalDist = calculateDistance(x, y, destx, desty);
        dx *= 5;
        dy *= 5;
        dx = Math.abs(dx);
        dy = Math.abs(dy);
    }

    public void rotate() {
        if (!atDestination()) return;
        rotateIndex++;
        if (rotateIndex >= ROTATIONS.length) {
            rotateIndex = 0;
        }
        destrad = ROTATIONS[rotateIndex];
    }

    public void randomRotate() {
        rotateIndex = MathUtils.random(0, 3);
        rad = destrad = ROTATIONS[rotateIndex];
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void moveToDestination(float dt) {
        if (x < destx) {
            x += dx * dt;
            if (x > destx) x = destx;
        }
        if (x > destx) {
            x -= dx * dt;
            if (x < destx) x = destx;
        }
        if (y < desty) {
            y += dy * dt;
            if (y > desty) y = desty;
        }
        if (y > desty) {
            y -= dy * dt;
            if (y < desty) y = desty;
        }
    }

    private  boolean atDestination() {
        return x == destx && y == desty && destrad == rad;
    }

    @Override
    public void update(float dt) {
        moveToDestination(dt);
        if (rad != destrad) {
            if (destrad == 0) {
                rad += 10 * dt;
                if (rad > MathUtils.PI2) rad = 0;
            } else {
                rad += 10 * dt;
                if (rad > destrad) rad = destrad;
            }
        }

        float distLeft = calculateDistance(x, y, destx, desty);
        float percent = distLeft / totalDist;
        jumpy = MathUtils.sin(MathUtils.PI * percent) * 70;
        scale = 1 + 1.5f * jumpy / 70;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, 1);
        Utils.drawRotatedScaled(sb, animation.getImage(), x, y, rad, scale);
    }
}
