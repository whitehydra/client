package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeDTO {
    @JsonProperty("typeID")
    private int typeID;
    @JsonProperty("name_type")
    private String name_type;
    @JsonProperty("sort_type")
    private String sort_type;

    TypeDTO(){}

    public int getTypeID() { return typeID; }
    public String getName_type() { return name_type; }
    public String getSort_type() { return sort_type; }


    public void setTypeID(int typeID) { this.typeID = typeID; }
    public void setName_type(String name_type) { this.name_type = name_type; }
    public void setSort_type(String sort_type) { this.sort_type = sort_type; }

    @Override
    public String toString(){
        return name_type;
    }
}
