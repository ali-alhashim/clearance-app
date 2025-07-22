package com.clearance.app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class ClearanceDto {

    private String badgeNumber;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastWorkingDate;

    private String directManagerEmail;
    private String accountsEmail;
    private String purchasingEmail;
    private String customerEmail;
    private String mobileEmail;
    private String vehicleEmail;
    private String securityEmail;
    private String itEmail;
    private String insuranceEmail;
    private String salesEmail;
    private String financeEmail;
    private String hrEmail;

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

    public String getDirectManagerEmail() {
        return directManagerEmail;
    }

    public String getAccountsEmail() {
        return accountsEmail;
    }

    public String getPurchasingEmail() {
        return purchasingEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getMobileEmail() {
        return mobileEmail;
    }

    public String getVehicleEmail() {
        return vehicleEmail;
    }

    public String getSecurityEmail() {
        return securityEmail;
    }

    public String getItEmail() {
        return itEmail;
    }

    public String getInsuranceEmail() {
        return insuranceEmail;
    }

    public String getSalesEmail() {
        return salesEmail;
    }

    public String getFinanceEmail() {
        return financeEmail;
    }

    public void setDirectManagerEmail(String directManagerEmail) {
        this.directManagerEmail = directManagerEmail;
    }

    public void setAccountsEmail(String accountsEmail) {
        this.accountsEmail = accountsEmail;
    }

    public void setPurchasingEmail(String purchasingEmail) {
        this.purchasingEmail = purchasingEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setMobileEmail(String mobileEmail) {
        this.mobileEmail = mobileEmail;
    }

    public void setVehicleEmail(String vehicleEmail) {
        this.vehicleEmail = vehicleEmail;
    }

    public void setSecurityEmail(String securityEmail) {
        this.securityEmail = securityEmail;
    }

    public void setItEmail(String itEmail) {
        this.itEmail = itEmail;
    }

    public void setInsuranceEmail(String insuranceEmail) {
        this.insuranceEmail = insuranceEmail;
    }

    public void setSalesEmail(String salesEmail) {
        this.salesEmail = salesEmail;
    }

    public void setFinanceEmail(String financeEmail) {
        this.financeEmail = financeEmail;
    }

    public String getHrEmail() {
        return hrEmail;
    }

    public void setHrEmail(String hrEmail) {
        this.hrEmail = hrEmail;
    }
}
