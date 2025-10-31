package com.distraction.comfyjam1025;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.audio.AudioHandler;
import com.distraction.comfyjam1025.screens.ScreenManager;

public class Context {

    private static final String ATLAS = "comfyjam1025.atlas";
    public static final String CON26 = "fonts/constantia26.fnt";
    public static final String PL56 = "fonts/PL56.fnt";

    public AssetManager assets;
    public AudioHandler audio;

    public ScreenManager sm;
    public SpriteBatch sb;

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.load(CON26, BitmapFont.class);
        assets.load(PL56, BitmapFont.class);
        assets.finishLoading();

        audio = new AudioHandler();

        sb = new SpriteBatch();

        sm = new ScreenManager(new com.distraction.comfyjam1025.screens.IntroScene(this));
//        sm = new ScreenManager(new com.distraction.comfyjam1025.screens.PlayScreen(this, 1));
//        sm = new ScreenManager(new com.distraction.comfyjam1025.screens.YearScreen(this, 1));
//        sm = new ScreenManager(new com.distraction.comfyjam1025.screens.GraveScene(this, 2));
    }

    public TextureRegion getImage(String key) {
        TextureRegion region = assets.get(ATLAS, TextureAtlas.class).findRegion(key);
        if (region == null) throw new IllegalArgumentException("image " + key + " not found");
        return region;
    }

    public TextureRegion getPixel() {
        return getImage("pixel");
    }

    public BitmapFont getFont(String name) {
        return getFont(name, 1f);
    }

    public BitmapFont getFont(String name, float scale) {
        BitmapFont originalFont = assets.get(name, BitmapFont.class);
        BitmapFont scaledFont = new BitmapFont(originalFont.getData().getFontFile(), originalFont.getRegion(), false);
        scaledFont.getData().setScale(scale);
        return scaledFont;
    }

    public void dispose() {
        sb.dispose();
    }

}
