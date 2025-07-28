package com.clearance.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Log {

    private String name;
    private String email;
    private LocalDateTime date;
    private String action;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
