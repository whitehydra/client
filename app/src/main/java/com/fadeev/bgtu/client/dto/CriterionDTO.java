package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriterionDTO {
    @JsonProperty("criterionID")
    private int criterionID;
    @JsonProperty("name_criterion")
    private String name_criterion;
    @JsonProperty("sort_criterion")
    private String sort_criterion;

    CriterionDTO(){}

    public int getCriterionID() { return criterionID; }
    public String getName_criterion() { return name_criterion; }
    public String getSort_criterion() { return sort_criterion; }


    public void setCriterionID(int criterionID) { this.criterionID = criterionID; }
    public void setName_criterion(String name_criterion) { this.name_criterion = name_criterion; }
    public void setSort_criterion(String sort_criterion) { this.sort_criterion = sort_criterion; }

    @Override
    public String toString(){
        return name_criterion;
    }
}
