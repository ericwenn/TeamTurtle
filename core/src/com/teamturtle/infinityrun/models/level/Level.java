package com.teamturtle.infinityrun.models.level;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ostmos on 2016-10-04.
 */
public class Level {

    private int id;
    private String url;
    private int[] categoryIDs;

    public Level() {

    }

    public Level(int id, String url, int[] categoryIDs) {
        this.id = id;
        this.url = url;
        this.categoryIDs = categoryIDs;

    }

    public String getUrl() {
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
        return categoryIDs;
    }

    public void setCategoryIDs(int[] categoryIDs) {
        this.categoryIDs = categoryIDs;
    }
}
