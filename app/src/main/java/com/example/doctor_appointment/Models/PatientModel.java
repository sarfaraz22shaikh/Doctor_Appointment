package com.example.doctor_appointment.Models;
public class PatientModel {

    private String email;
    private String gender;
    private String mobile;
    private String name;
    private boolean profileCompleted;
    private String role;
    private String userId;
    private String bloodGroup;
    private String location;
    private String dob;
    private String profileImageUrl;

    // Default constructor
    public PatientModel() {
        // Empty constructor for Firebase
    }
    // Constructor with all fields
    public PatientModel(String email, String gender, String mobile, String name,
                        boolean profileCompleted, String role, String userId,
                        String bloodGroup, String location, String dob,String profileImageUrl) {
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.name = name;
        this.profileCompleted = profileCompleted;
        this.role = role;
        this.userId = userId;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.dob = dob;
        this.profileImageUrl = profileImageUrl;
    }

    // Getter and Setter Methods
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
