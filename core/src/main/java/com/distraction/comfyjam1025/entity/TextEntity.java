package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.distraction.comfyjam1025.Context;

import java.util.Objects;

public class TextEntity extends Entity {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private final GlyphLayout glyphLayout;
    private final BitmapFont font;

    public Alignment alignment = Alignment.LEFT;

    private String currentText = "";
    private final Color color = new Color(1, 1, 1, 1);

    public float ta = 1;

    public TextEntity(Context context, BitmapFont font, String text, float x, float y, Alignment alignment) {
        this(context, font, text, x, y);
        this.alignment = alignment;
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
        if (alignment == Alignment.CENTER) {
            font.draw(sb, glyphLayout, x - glyphLayout.width / 2f, y + glyphLayout.height / 2f);
        } else if (alignment == Alignment.LEFT) {
            font.draw(sb, glyphLayout, x, y + glyphLayout.height / 2f);
        } else {
            font.draw(sb, glyphLayout, x - w + 1, y + glyphLayout.height / 2f);
        }
    }

}
