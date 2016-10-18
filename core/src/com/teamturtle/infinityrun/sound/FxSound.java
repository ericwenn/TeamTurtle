package com.teamturtle.infinityrun.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public enum FxSound implements Disposable {

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
    MISSLYCKANDE8("gornyttforsok"), MISSLYCKANDE9("intebra"), MISSLYCKANDE10("vadtrakigt"),
    EJUPPLAST("ejupplast"), REDOKOR("redokor"), REDO("redo2"), FORSOKIGEN("forsokigen"),
    NASTABANA("nastabana"), HEM("hem"), MALGANG1("brajobbat"), MALGANG2("brajobbat2"),
    MALGANG3("duarimal"), MALGANG4("duklaradebanan"), MALGANG5("duklaradedet"),
    MALGANG6("lostesnyggt"), MALGANG7("snyggtdar"), RIGHT_ANSWER("right_answer"),
    WRONG_ANSWER("wrong_answer");

    private static Sound[] levelSounds, failureSounds;
    private static final int LEVEL_AMOUNT = 15;
    private static final int DEFAULT_VOLUME = 1;
    private static final String URL_PREFIX = "audio/feedback/";
    private static final String URL_SUFFIX = ".mp3";
    private static boolean fxMuted = false;
    private static AssetManager assetManager = new AssetManager();
    private String url;

    FxSound(String url) {
        this.url = URL_PREFIX + url + URL_SUFFIX;
    }

    public void play(float volume) {
        if (!fxMuted) {
            Sound sound = assetManager.get(url, Sound.class);
            sound.play(volume);
        }
    }

    public void play() {
        play(DEFAULT_VOLUME);
    }

    public static void playLevelSound(int level) {
        if (!fxMuted) {
            if (level <= LEVEL_AMOUNT) {
                int levelIndex = level - 1;
                levelSounds[levelIndex].play();
            } else {
                Gdx.app.error("AUDIO", "Audiofile for level " + level + " not found");
            }
        }
    }

    public static void load() {
        for (FxSound fxSound : values()) {
            assetManager.load(fxSound.url, Sound.class);
        }
    }

    private static void initLevelAudioArray() {
        levelSounds = new Sound[LEVEL_AMOUNT];
        for (int i = 0; i < LEVEL_AMOUNT; i++) {
            levelSounds[i] = assetManager.get(values()[i].url, Sound.class);
        }
    }

    private static void initFailureAudioArray() {
        failureSounds = new Sound[10];
    }

    public static void shiftFxMute() {
        fxMuted = fxMuted ? false : true;
    }

    public static Sound getSound(FxSound fxSound) {
        return assetManager.get(fxSound.url, Sound.class);
    }

    @Override
    public void dispose() {
        for (FxSound sound: values()) {
            sound.dispose();
        }
        assetManager.dispose();
    }

    public static boolean updateLoading() {
        if (assetManager.update()) initLevelAudioArray();
        return assetManager.update();
    }

    public static boolean isFxMuted() {
        return fxMuted;
    }
}
