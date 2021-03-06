package com.teamturtle.infinityrun.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Henrik on 2016-10-16.
 */
public enum GameMusic {

    THEME_1("TeamTurtleTheme1");

    private static final String URL_PREFIX = "audio/music/";
    private static final String URL_SUFFIX = ".mp3";
    private static boolean musicMuted = false;
    private static final float VOLUME = 0.12f;
    private Music music;

    GameMusic(String musicName) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(URL_PREFIX + musicName + URL_SUFFIX));
        this.music.setVolume(VOLUME);
    }

    public void playMusicLooping() {
        if (!musicMuted) {
            for (GameMusic gameMusic: values()) {
                gameMusic.music.stop();
            }
            music.setLooping(true);
            music.play();
        }

    }

    public static void shiftMusicMute() {
        musicMuted = !musicMuted;
        if (musicMuted) {
            for(GameMusic gameMusic : values()) {
                gameMusic.music.setVolume(0);
            }
        }else{
            for (GameMusic gameMusic : values()) {
                gameMusic.music.setVolume(VOLUME);
            }
        }
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }
}
