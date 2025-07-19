package com.clearance.app.model;

import java.time.LocalDateTime;

public class Approval {

    private String approverUserId; // department manager's user id

    private boolean approved;

    private String note; // note added by manager

    private LocalDateTime approvedAt;

    private String otpCode; // OTP pending or confirmed

    public String getApproverUserId() {
        return approverUserId;
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

    public void setApproverUserId(String approverUserId) {
        this.approverUserId = approverUserId;
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
