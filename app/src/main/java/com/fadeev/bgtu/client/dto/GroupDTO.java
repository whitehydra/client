package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupDTO {
    @JsonProperty("groupID")
    private int groupID;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("fullName")
    private String fullName;

    public GroupDTO(){}

    public GroupDTO(int groupID, String shortName, String fullName){
        this.groupID = groupID;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public int getGroupID() { return groupID; }
    public String getShortName() { return shortName; }
    public String getFullName() { return fullName; }

    public void setGroupID(int groupID) { this.groupID = groupID; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public String toString(){
        return shortName;
    }
}
