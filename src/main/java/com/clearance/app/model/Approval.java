package com.clearance.app.model;

import java.time.LocalDateTime;

public class Approval {

    private String approvalEmail; // Approval Email

    private String name;          // Approval Name

    private String department;     // approval department

    private boolean approved;      // status for the approval

    private String note; // note added by approval

    private LocalDateTime approvedAt; // date for approval

    private String otpCode; // OTP sent for approval

    public String getApprovalEmail() {
        return approvalEmail;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setApprovalEmail(String approvalEmail) {
        this.approvalEmail = approvalEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
