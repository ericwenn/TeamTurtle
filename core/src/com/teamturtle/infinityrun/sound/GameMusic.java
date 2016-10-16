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
    private Music music;

    GameMusic(String musicName) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(URL_PREFIX + musicName + URL_SUFFIX));
        this.music.setVolume(0.1f);
    }

    public void playMusicLooping() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!musicMuted) {
                    for (GameMusic gameMusic: values()) {
                        gameMusic.music.stop();
                    }
                    music.setLooping(true);
                    music.play();
                }
            }
        });
        thread.start();
    }

    public static void shiftMusicMute() {
        musicMuted = musicMuted ? false : true;
        if (musicMuted) {
            for(GameMusic gameMusic : values()) {
                gameMusic.music.setVolume(0);
            }
        }else{
            for (GameMusic gameMusic : values()) {
                gameMusic.music.setVolume(0.1f);
            }
        }
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }
}
