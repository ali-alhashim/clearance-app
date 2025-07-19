package com.clearance.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "clearances")
public class Clearance {

    @Id
    private String id;

    private String badgeNumber; //the employee exit from the company

    private String createdByUserId; // HR user who created this

    private LocalDateTime createdAt;

    private String status; //  "PENDING", "APPROVED", "REJECTED"

    private List<Approval> approvals; // each department/manager approval => AppUser

    private String notes; // general HR notes or summary


    public String getId() {
        return id;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
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

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
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
}
