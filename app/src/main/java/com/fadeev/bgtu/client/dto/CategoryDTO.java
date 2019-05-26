package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDTO {
    @JsonProperty("categoryID")
    private int categoryID;
    @JsonProperty("name_category")
    private String name_category;

    public CategoryDTO(){}

    public CategoryDTO(int categoryID, String name_category){
        this.categoryID = categoryID;
        this.name_category = name_category;
    }

    public int getCategoryID() { return categoryID; }
    public String getName_category() { return name_category; }

    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    public void setName_category(String name_category) { this.name_category = name_category; }


    @Override
    public String toString(){
        return name_category;
    }
}
