package com.teamturtle.infinityrun.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer
{
    public static void playSound(String filename, String type)
    {
        Sound sound;
        if(type.equals("feedback"))
            sound = Gdx.audio.newSound(Gdx.files.internal("audio/feedback/" + filename + ".mp3"));
        else
            sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + filename + ".mp3"));
        sound.play();
    }
}
