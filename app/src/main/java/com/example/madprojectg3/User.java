package com.example.madprojectg3;

public class User {
    private String username;
    private String phone;
    private String email;
    private String dob;
    private String skinType;
    private String password; // Add the password field

    // Constructor
    public User(String username, String phone, String email, String dob, String skinType, String password) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.skinType = skinType;
        this.password = password; // Initialize the password
    }

    // Getters and Setters (optional, depending on your usage)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}