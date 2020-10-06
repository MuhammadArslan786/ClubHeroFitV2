package com.arslan6015.clubherofitv2.Model;

public class UserGeneralInfo {
    private String id,FullName,email,image;

    public UserGeneralInfo() {
    }

    public UserGeneralInfo(String id, String fullName, String email) {
        this.id = id;
        FullName = fullName;
        this.email = email;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
