package com.teamturtle.infinityrun.models.category;

/**
 * Created by Henrik on 2016-10-18.
 */
public class CategoryImpl implements Category{

    private int id;
    private String categoryName;

    public CategoryImpl() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String getName() {
        return categoryName;
    }

    @Override
    public int getId() {
        return id;
    }
}
