package com.arslan6015.clubherofitv2.Model;

public class UserGeneralInfo {
    private String FullName,email;

    public UserGeneralInfo(String fullName, String email) {
        FullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
