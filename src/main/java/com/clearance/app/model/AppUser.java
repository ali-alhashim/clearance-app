package com.clearance.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class AppUser {

    @Id
    private String id;
    private String name;
    private String email;
    private String otpCode;
    private String role;             //USER, ADMIN
    private String department;       // IT, HR, SECURITY, INSURANCE, VEHICLE, PURCHASING, ACCOUNTS, CUSTOMER-SERVICE, SALES, FINANCE
    private boolean isManager = false;
    private boolean active;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
