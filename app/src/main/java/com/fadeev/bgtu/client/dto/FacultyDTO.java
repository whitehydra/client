package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacultyDTO {
    @JsonProperty("facultyID")
    private int facultyID;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("fullName")
    private String fullName;

    public FacultyDTO(){}

    public FacultyDTO(int facultyID, String shortName, String fullName){
        this.facultyID = facultyID;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public int getFacultyID() { return facultyID; }
    public String getShortName() { return shortName; }
    public String getFullName() { return fullName; }

    public void setFacultyID(int facultyID) { this.facultyID = facultyID; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public String toString(){
        return shortName;
    }
}
