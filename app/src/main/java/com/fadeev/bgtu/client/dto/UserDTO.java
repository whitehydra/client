package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserDTO implements Serializable{


    @JsonProperty("name")
    private String name;
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
    @JsonProperty("avatar")
    private String avatar;

    public UserDTO(){ }

    public String getName() { return name; }
    public String getLevel() {
        return level;
    }
    public String getFaculty() { return faculty; }
    public String getStudyGroup() { return studyGroup; }
    public String getPhone() { return phone; }
    public String getMail() { return mail; }
    public String getInfo() { return info; }
    public String getAvatar() { return avatar; }

    public void setName(String name) { this.name = name; }
    public void setLevel(String level) { this.level = level; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public void setStudyGroup(String studyGroup) { this.studyGroup = studyGroup; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setMail(String mail) { this.mail = mail; }
    public void setInfo(String info) { this.info = info; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
