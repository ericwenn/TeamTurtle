package com.teamturtle.infinityrun.models.category;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Henrik on 2016-10-18.
 */
public class CategoryLoader {

    private final ArrayList<Category> categories;

    public CategoryLoader() {
        Json json = new Json();
        FileHandle file = Gdx.files.internal("data/categories.json");
        categories = json.fromJson(ArrayList.class, CategoryImpl.class, file.readString());
    }

    public String getCategoryName(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category.getName();
            }
        }
        return "None category";
    }

}