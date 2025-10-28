package com.distraction.comfyjam1025.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

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
    private Color color = Color.WHITE;

    public TextEntity(BitmapFont font, String text, float x, float y, Alignment alignment) {
        this(font, text, x, y);
        this.alignment = alignment;
    }

    public TextEntity(BitmapFont font, String text, float x, float y) {
        this.font = font;
        glyphLayout = new GlyphLayout();
        setText(text);
        this.x = x;
        this.y = y;
    }

    public void setText(String text) {
        if (!Objects.equals(currentText, text)) {
            currentText = text;
            glyphLayout.setText(font, currentText, 0, currentText.length(), color, 0, Align.left, false, null);
            w = glyphLayout.width;
            h = glyphLayout.height;
        }
    }

    public void setColor(Color color) {
        if (color != this.color) {
            this.color = color;
            glyphLayout.setText(font, currentText, 0, currentText.length(), color, 0, Align.left, false, null);
        }
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
