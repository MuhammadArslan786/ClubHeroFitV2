package com.arslan6015.clubherofitv2.Model;

public class ContactInfo {

    private String id,fullName,email,image;

    public ContactInfo() {
    }

    public ContactInfo(String id, String fullName, String email, String image) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
