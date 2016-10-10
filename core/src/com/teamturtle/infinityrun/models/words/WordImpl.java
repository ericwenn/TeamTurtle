package com.teamturtle.infinityrun.models.words;

public class WordImpl implements Word{
    public String id;
    public String word;
    public String category;
    public String filename;
    public String soundUrl;

    public WordImpl() {
    }

    @Override
    public String getText() {
        return word;
    }

    @Override
    public String getSoundUrl() {
        if(soundUrl != null)
            return "audio/" + soundUrl + ".mp3";
        else
            return "404";
    }

    @Override
    public String getIconUrl() {
        return "emoji/good/" + filename + ".png";
    }

    @Override
    public int getCategory() {
        return Integer.parseInt(category);
    }

    @Override
    public int getId() {
        return Integer.parseInt(id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 7 * hash + (getIconUrl().hashCode());
        hash = 7 * hash + (getText() == null ? 0 : getText().hashCode());
        hash = 7 * hash + (getSoundUrl().hashCode());
        return hash;
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
