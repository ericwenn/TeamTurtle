package com.teamturtle.infinityrun.models.level;

/**
 * Created by ostmos on 2016-10-04.
 */
public class LevelImpl implements Level {

    private int id;
    private String url;
    private int[] categoryIDs;

    public LevelImpl() {

    }

    public LevelImpl(int id, String url, int[] categoryIDs) {
        this.id = id;
        this.url = url;
        this.categoryIDs = categoryIDs == null ? null : categoryIDs.clone();

    }

    public String getMapUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getCategoryIDs() {
        return categoryIDs.clone();
    }

    public void setCategoryIDs(int[] categoryIDs) {
        this.categoryIDs = categoryIDs == null ? null : categoryIDs.clone();
    }
}
