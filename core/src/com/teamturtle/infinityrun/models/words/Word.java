package com.teamturtle.infinityrun.models.words;

/**
 * A word consists of a textual representation, along with an icon describing the word and a pronounciation.
 * {@link Word#getSoundUrl()} and {@link Word#getIconUrl()} represents file paths to the files on the device.
 */
public interface Word {

    String getText();

    String getSoundUrl();

    String getIconUrl();

    String getArticle();

    int getCategory();

    int getId();

}
