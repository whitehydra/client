package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserDTO implements Serializable{

    @JsonProperty("id_usr")
    private int id_usr;

    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("password")
    private String password;
    @JsonProperty("level")
    private String level;
    @JsonProperty("faculty")
    private String faculty;
    @JsonProperty("studyGroup")
    private String studyGroup;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("info")
    private String info;

    public UserDTO(){ }

    public int getId_usr() { return id_usr; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getLevel() {
        return level;
    }
    public String getFaculty() { return faculty; }
    public String getStudyGroup() { return studyGroup; }
    public String getPhone() { return phone; }
    public String getMail() { return mail; }
    public String getInfo() { return info; }

    public void setId_usr(int id_usr) { this.id_usr = id_usr; }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setLevel(String level) { this.level = level; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public void setStudyGroup(String studyGroup) { this.studyGroup = studyGroup; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setMail(String mail) { this.mail = mail; }
    public void setInfo(String info) { this.info = info; }
}
