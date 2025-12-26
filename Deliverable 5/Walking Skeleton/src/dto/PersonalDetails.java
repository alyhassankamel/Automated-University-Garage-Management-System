package com.augms.dto;

public class PersonalDetails {
    private String uniID;
    private String name;
    private String email;
    private String phone;
    
    public PersonalDetails() {}
    
    public PersonalDetails(String uniID, String name, String email, String phone) {
        this.uniID = uniID;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and setters
    public String getUniID() { return uniID; }
    public void setUniID(String uniID) { this.uniID = uniID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

