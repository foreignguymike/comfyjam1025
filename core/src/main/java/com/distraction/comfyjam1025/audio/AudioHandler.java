package com.distraction.comfyjam1025.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioHandler {

    private final Map<String, Music> music;
    private final Map<String, Sound> sounds;

    private MusicConfig currentlyPlaying;

    public AudioHandler() {
        music = new HashMap<>();
        addMusic("forgotten", "music/forgotten.ogg");
        addMusic("remembered", "music/remembered.ogg");

        sounds = new HashMap<>();
        addSound("step", "sfx/step.ogg");
        addSound("swap", "sfx/swap.ogg");
        addSound("puzzlefinish", "sfx/puzzlefinish.ogg");
    }

    private void addMusic(String key, String fileName) {
        music.put(key, Gdx.audio.newMusic(Gdx.files.internal(fileName)));
    }

    private void addSound(String key, String fileName) {
        sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(fileName)));
    }

    public void playMusic(String key, float volume, boolean looping) {
        Music newMusic = music.get(key);
        if (newMusic == null) {
            System.out.println("unknown music: " + key);
            return;
        }
        currentlyPlaying = new MusicConfig(music.get(key), volume, looping);
        currentlyPlaying.play();
    }

    public boolean isMusicPlaying() {
        return currentlyPlaying != null && currentlyPlaying.isPlaying();
    }

    public void stopMusic() {
        if (currentlyPlaying != null) {
            currentlyPlaying.stop();
            currentlyPlaying = null;
        }
    }

    public void playSound(String key) {
        playSound(key, 1, false);
    }

    public void playSound(String key, float volume) {
        playSound(key, volume, false);
    }

    public void playSound(String key, float volume, boolean cut) {
        for (Map.Entry<String, Sound> entry : sounds.entrySet()) {
            if (entry.getKey().equals(key)) {
                if (cut) entry.getValue().stop();
                entry.getValue().play(volume);
            }
        }
    }

}
