package com.abio.csv.model;

public class BlacklistedCustomer {
    private Long Id;
    private String email;
    private String phoneNumber;
    private String reason;
    private String blacklistedAt;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BlacklistedCustomer() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(String blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }
}
