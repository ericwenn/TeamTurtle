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

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof Word)) {
            return false;
        }

        if( obj == this) {
            return true;
        }

        Word rhs = (Word) obj;
        return rhs.getText().equals(this.getText())
                && rhs.getIconUrl().equals(this.getIconUrl())
                && rhs.getSoundUrl().equals(this.getSoundUrl());

    }
}
