package com.example.doctor_appointment.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.doctor_appointment.Repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends ViewModel {
    private AuthRepository authRepository;
    private LiveData<FirebaseUser> userLiveData;
    private LiveData<String> authError;

    public RegisterViewModel() {
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
        authError = authRepository.getAuthError();
    }

    public void registerPatient(String name, String email, String mobile, String password, String gender) {
        authRepository.registerUser(name, email, mobile, password, gender);
    }
    public void registerDoctor(String name, String email, String mobile, String password, String gender) {
        authRepository.registerDoctor(name, email, mobile, password, gender);
    }
    public void registerDoctorDetails(String specialization, String experience, String Location) {
        authRepository.registerDoctorDetails(specialization,experience,Location);
    }

    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<String> getAuthError() { return authError; }
}
