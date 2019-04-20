package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDTO {
    @JsonProperty("file_name")
    private String file_name;
    @JsonProperty("file_src")
    private String file_src;
    @JsonProperty("file_type")
    private String file_type;
    @JsonProperty("id_portfolio")
    private int id_portfolio;


    public FileDTO(){}

    public FileDTO(String file_name, String file_src, String file_type, int id_portfolio){
        this.file_name = file_name;
        this.file_src = file_src;
        this.file_type = file_type;
        this.id_portfolio = id_portfolio;
    }


    public String getFile_name() { return file_name; }
    public String getFile_src() { return file_src; }
    public String getFile_type() { return file_type; }
    public int getId_portfolio() { return id_portfolio; }

    public void setFile_name(String file_name) { this.file_name = file_name; }
    public void setFile_src(String file_src) { this.file_src = file_src; }
    public void setFile_type(String file_type) { this.file_type = file_type; }
    public void setId_portfolio(int id_portfolio) { this.id_portfolio = id_portfolio; }
}
