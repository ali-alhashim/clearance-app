package com.clearance.app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class ClearanceDto {

    private String badgeNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastWorkingDate;

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public LocalDate getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setLastWorkingDate(LocalDate lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }
}
