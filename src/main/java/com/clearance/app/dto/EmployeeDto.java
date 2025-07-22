package com.clearance.app.dto;

public class EmployeeDto {

    private String badgeNumber;

    private String location;

    private String name;

    private String arName;

    private String department;

    private String jobTitle;

    private String email;

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getArName() {
        return arName;
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

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArName(String arName) {
        this.arName = arName;
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
}
