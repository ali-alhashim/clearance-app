package com.clearance.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "clearances")
public class Clearance {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private LocalDate lastWorkingDate;

    private String badgeNumber; //the employee exit from the company
    private String name;
    private String arName;
    private String department;
    private String jobTitle;

    private String createdByUserEmail; // HR user who created this

    private LocalDateTime createdAt;

    private String status; //  "PENDING", "APPROVED", "REJECTED"

    private List<Approval> approvals; // each department/manager approval => AppUser

    private List<Comment> comments; // each clearance has many Comments

    private String notes; // general HR notes or summary


    public String getId() {
        return id;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public List<Approval> getApprovals() {
        return approvals;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }



    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApprovals(List<Approval> approvals) {
        this.approvals = approvals;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLastWorkingDate(LocalDate lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
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

    public String getCreatedByUserEmail() {
        return createdByUserEmail;
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

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
