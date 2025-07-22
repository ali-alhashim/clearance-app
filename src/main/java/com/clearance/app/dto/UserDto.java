package com.clearance.app.dto;

public class UserDto {

    private String name;
    private String email;
    private String role;
    private String department;
    private boolean isManager = false;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
