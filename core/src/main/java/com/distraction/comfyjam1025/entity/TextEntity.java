package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Context;

import java.util.Objects;

public class TextEntity extends Entity {

    public enum HAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    private final GlyphLayout glyphLayout;
    private final BitmapFont font;

    public HAlignment hAlignment = HAlignment.LEFT;
    public VAlignment vAlignment = VAlignment.TOP;

    private String currentText = "";
    private final Color color = new Color(1, 1, 1, 1);

    public float ta = 1;

    public TextEntity(Context context, BitmapFont font, String text, float x, float y, HAlignment hAlignment) {
        this(context, font, text, x, y);
        this.hAlignment = hAlignment;
    }

    public TextEntity(Context context, BitmapFont font, String text, float x, float y, HAlignment hAlignment, VAlignment vAlignment) {
        this(context, font, text, x, y);
        this.hAlignment = hAlignment;
        this.vAlignment = vAlignment;
    }

    public TextEntity(Context context, BitmapFont font, String text, float x, float y) {
        super(context);
        this.font = font;
        glyphLayout = new GlyphLayout();
        setText(text);
        this.x = x;
        this.y = y;
    }

    public void setText(String text) {
        if (!Objects.equals(currentText, text)) {
            currentText = text;
            glyphLayout.setText(font, currentText);
            w = glyphLayout.width;
            h = glyphLayout.height;
        }
    }

    public void setColor(float r, float g, float b, float a) {
        if (color.r != r || color.g != g || color.b != b || color.a != a) {
            color.set(r, g, b, a);
            font.setColor(color);
            glyphLayout.setText(font, currentText);
        }
    }

    @Override
    public void update(float dt) {
        if (a < ta) {
            a += dt * 2;
        } else if (a > ta) {
            a -= dt * 2;
        }
        a = MathUtils.clamp(a, 0, 1);
        setColor(1, 1, 1, a);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (currentText.isEmpty()) return;

        float tx, ty;
        if (hAlignment == HAlignment.CENTER) tx = x - w / 2f;
        else if (hAlignment == HAlignment.LEFT) tx = x;
        else tx = x - w;
        if (vAlignment == VAlignment.CENTER) ty = y + h / 2f;
        else if (vAlignment == VAlignment.TOP) ty = y;
        else ty = y + h;

        font.draw(sb, glyphLayout, tx, ty);
    }

}
