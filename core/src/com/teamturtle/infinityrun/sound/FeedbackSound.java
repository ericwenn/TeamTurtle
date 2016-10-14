package com.teamturtle.infinityrun.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public enum FeedbackSound implements Disposable {

    BANA1("bana1"), BANA2("bana2"), BANA3("bana3"), BANA4("bana4"), BANA5("bana5"),
    BANA6("bana6"), BANA7("bana7"), BANA8("bana8"), BANA9("bana9"), BANA10("bana10"),
    BANA11("bana11"), BANA12("bana12"), BANA13("bana13"), BANA14("bana14"), BANA15("bana15"),
    BANA16("bana16"), BANOR("banor"), BRAJOBBAT("brajobbat"),
    DUARIMAL("duarimal"), DUDOG("dudog"), DUDOG2("dudog2"), DUKLARADEDET("duklaradedet"),
    FELGISSAT("felgissat"), FRAGA("fraga"), HARARENFRAGA("hararenfraga"),
    KLICKAFORLJUD("klickaforljud"), KOR("kor"), ORDLISTA("ordlista"), RATTGISSAT("rattgissat"),
    RATTSVARVAR("rattsvarvar"), SNYGGTDAR("snyggtdar"), SPELA("spela3"), TILLBAKA("tillbaka"),
    VALJBANA("valjbana"), MISSLYCKANDE1("battrelyckanastagang"),
    MISSLYCKANDE2("detdarkandugorabattre"), MISSLYCKANDE3("duforlorade"),
    MISSLYCKANDE4("duklaradeintebanan"), MISSLYCKANDE5("duklaradeintebanan2"),
    MISSLYCKANDE6("duklaradeintebanan3"), MISSLYCKANDE7("dumisslyckades"),
    MISSLYCKANDE8("gornyttforsok"), MISSLYCKANDE9("intebra"), MISSLYCKANDE10("vadtrakigt");

    private Sound sound;
    private static Sound[] levelSounds;
    private static final int LEVEL_AMOUNT = 15;
    private static final String URL_PREFIX = "audio/feedback/";
    private static final String URL_SUFFIX = ".mp3";

    FeedbackSound(String filename) {
        sound = Gdx.audio.newSound(Gdx.files.internal(URL_PREFIX + filename + URL_SUFFIX));
    }

    public void play() {
        sound.play();
    }

    public void play(float volume) {
        sound.play(volume);
    }

    public static void playLevelSound(int level) {
        if (level <= LEVEL_AMOUNT) {
            int levelIndex = level - 1;
            levelSounds[levelIndex].play();
        } else {
            Gdx.app.error("AUDIO", "Audiofile for level " + level + " not found");
        }
    }

    public static void load() {
        values();
        levelSounds = new Sound[LEVEL_AMOUNT];
        for (int i = 0; i < LEVEL_AMOUNT; i++) {
            levelSounds[i] = values()[i].sound;
        }
    }


    @Override
    public void dispose() {
        for (FeedbackSound sound: values()) {
            sound.dispose();
        }
    }
}
