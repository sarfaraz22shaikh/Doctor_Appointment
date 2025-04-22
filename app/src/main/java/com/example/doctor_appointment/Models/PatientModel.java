package com.example.doctor_appointment.Models;

public class PatientModel {
    private String email;
    private String gender;
    private String mobile;
    private String name;
    private boolean profileCompleted;
    private String role;
    private String userId;

    public PatientModel(String email, String gender, String mobile, String name,
                        boolean profileCompleted, String role, String userId) {
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.name = name;
        this.profileCompleted = profileCompleted;
        this.role = role;
        this.userId = userId;
    }
    public PatientModel(){ }
    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
// Other getters if needed...
}
