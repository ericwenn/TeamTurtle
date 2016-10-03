package com.teamturtle.infinityrun.models;

/**
 * Created by ericwenn on 10/2/16.
 */
public class WordImpl implements Word {
    private String emojiName;
    private String soundUrl;
    private String iconUrl;

    public WordImpl(String emojiName, String soundUrl, String iconUrl) {
        this.emojiName = emojiName;
        this.soundUrl = soundUrl;
        this.iconUrl = iconUrl;
    }


    @Override
    public String getText() {
        return emojiName;
    }

    @Override
    public String getSoundUrl() {
        return soundUrl;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }
}
