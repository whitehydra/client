package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PortfolioDTO {
    @JsonProperty("id_portfolio")
    protected int id_portfolio;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("date_event")
    protected String date_event;
    @JsonProperty("date_publication")
    protected String date_publication;

    @JsonProperty("id_category")
    private int id_category;
    @JsonProperty("id_criterion")
    private int id_criterion;
    @JsonProperty("id_type")
    private int id_type;

    @SerializedName("category")
    @Expose
    private CategoryDTO category;
    @SerializedName("criterion")
    @Expose
    private CriterionDTO criterion;
    @SerializedName("type")
    @Expose
    private TypeDTO type;

    public PortfolioDTO(){}

    public PortfolioDTO(String name, String date_event, String date_publication, int id_category, int id_criterion, int id_type){
        this.name = name;
        this.date_event = date_event;
        this.date_publication = date_publication;
        this.id_category = id_category;
        this.id_criterion = id_criterion;
        this.id_type = id_type;
    }

    public PortfolioDTO(String name, String date_event, CategoryDTO category, TypeDTO type){
        this.name = name;
        this.date_event = date_event;
        this.category = category;
        this.type = type;
    }

    public int getId_portfolio() { return id_portfolio; }
    public String getName() { return name; }
    public String getDate_event() { return date_event; }
    public String getDate_publication() { return date_publication; }
    public int getId_category() { return id_category; }
    public int getId_criterion() { return id_criterion; }
    public int getId_type() { return id_type; }
    public CategoryDTO getCategory() { return category; }
    public CriterionDTO getCriterion() { return criterion; }
    public TypeDTO getType() { return type; }

    public void setId_portfolio(int id_portfolio) { this.id_portfolio = id_portfolio; }
    public void setName(String name) { this.name = name; }
    public void setDate_event(String date_event) { this.date_event = date_event; }
    public void setDate_publication(String date_publication) { this.date_publication = date_publication; }
    public void setId_category(int id_category) { this.id_category = id_category; }
    public void setId_criterion(int id_criterion) { this.id_criterion = id_criterion; }
    public void setId_type(int id_type) { this.id_type = id_type; }
    public void setId_category(CategoryDTO id_category) { this.category = id_category; }
    public void setId_criterion(CriterionDTO id_criterion) { this.criterion = id_criterion; }
    public void setId_type(TypeDTO id_type) { this.type = id_type; }
}
