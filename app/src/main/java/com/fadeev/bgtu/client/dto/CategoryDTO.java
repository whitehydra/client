package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDTO {
    @JsonProperty("categoryID")
    private int categoryID;
    @JsonProperty("name_category")
    private String name_category;
    @JsonProperty("sort_category")
    private String sort_category;

    public CategoryDTO(){}

    public int getCategoryID() { return categoryID; }
    public String getName_category() { return name_category; }
    public String getSort_category() { return sort_category; }

    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    public void setName_category(String name_category) { this.name_category = name_category; }
    public void setSort_category(String sort_category) { this.sort_category = sort_category; }


    @Override
    public String toString(){
        return name_category;
    }
}
