package com.clearance.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {

    @Id
    private String id;

    private String logo;
    private String name;
    private String arName;
    private String cr;

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getArName() {
        return arName;
    }

    public String getCr() {
        return cr;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }
}
