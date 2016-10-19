package com.teamturtle.infinityrun.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Henrik on 2016-10-16.
 */
public class GameMusic {

    private static final String URL_PREFIX = "audio/music/";
    private static final String URL_SUFFIX = ".mp3";
    private static boolean musicMuted = false;


    public enum Theme {
        THEME_1("TeamTurtleTheme1");
        private Music music;
        Theme(String musicName) {
            this.music = Gdx.audio.newMusic(Gdx.files.internal(URL_PREFIX + musicName + URL_SUFFIX));
            this.music.setVolume(0.03f);
        }

    }



    public static void playMusicLooping(final Theme theme ) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!musicMuted) {
                    for (Theme theme: Theme.values()) {
                        theme.music.stop();
                    }
                    theme.music.setLooping(true);
                    theme.music.play();
                }
            }
        });
        thread.start();
    }

    public static void shiftMusicMute() {
        musicMuted = !musicMuted;
        if (musicMuted) {
            for(Theme theme : Theme.values()) {
                theme.music.setVolume(0);
            }
        }else{
            for (Theme theme : Theme.values()) {
                theme.music.setVolume(0.03f);
            }
        }
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }
}
