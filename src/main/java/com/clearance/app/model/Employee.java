package com.clearance.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    private String badgeNumber;

    private String location;

    private String name;

    private String arName;

    private String department;

    private String jobTitle;

    private String email;

    private boolean active;

    public String getId() {
        return id;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }



    public String getDepartment() {
        return department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }



    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getArName() {
        return arName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }
}
