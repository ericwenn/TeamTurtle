package com.teamturtle.infinityrun.models;

/**
 * Created by ericwenn on 10/2/16.
 */
public class EmojiModel {
    private String emojiName;
    private String soundUrl;
    private String iconUrl;

    public EmojiModel(String emojiName, String soundUrl, String iconUrl) {
        this.emojiName = emojiName;
        this.soundUrl = soundUrl;
        this.iconUrl = iconUrl;
    }

    public String getEmojiName() {
        return emojiName;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
