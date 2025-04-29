package com.example.doctor_appointment.Repository;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doctor_appointment.Activites.DoctorDashboard;
import com.example.doctor_appointment.Activites.PatientDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AuthRepository extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<String> authError;
    private MutableLiveData<Boolean> loginSuccess;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userLiveData = new MutableLiveData<>();
        authError = new MutableLiveData<>();
    }

    // Register new user
    public void registerUser(String name, String email, String mobile, String password, String gender) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        saveUserToFirestore(userId, name, email, mobile, gender);
                    } else {
                        authError.setValue(task.getException().getMessage());
                    }
                });
    }
    public void registerDoctor(String name, String email, String mobile, String password, String gender) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        saveDoctorToFirestore(userId,name,email,mobile,gender);
                    } else {
                        authError.setValue(task.getException().getMessage());
                    }
                });
    }
    public void loginUser(String email, String password, Activity activity) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // 🔍 First, check if the user is in the Doctors collection
                            db.collection("Doctors").document(uid).get()
                                    .addOnSuccessListener(doctorSnapshot -> {
                                        if (doctorSnapshot.exists()) {
                                            // ✅ User is a doctor
                                            Intent intent = new Intent(activity, DoctorDashboard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            activity.startActivity(intent);
                                        } else {
                                            // 🔍 If not found in Doctors, check Patients collection
                                            db.collection("Patients").document(uid).get()
                                                    .addOnSuccessListener(patientSnapshot -> {
                                                        if (patientSnapshot.exists()) {
                                                            // ✅ User is a patient
                                                            Intent intent = new Intent(activity, PatientDashboard.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            activity.startActivity(intent);
                                                        } else {
                                                            // 🚨 User does not exist in either collection
                                                            Toast.makeText(activity, "User role not found.", Toast.LENGTH_LONG).show();
                                                            auth.signOut(); // Logout user to prevent unauthorized access
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(activity, "Error checking Patients: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(activity, "Error checking Doctors: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        Toast.makeText(activity, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void registerDoctorDetails(String specialization,String experience,String location){
        Map<String, Object> details = new HashMap<>();
        details.put("experience",experience);
        details.put("specialization",specialization);
        details.put("location",location);
        String userId = auth.getCurrentUser().getUid();
        db.collection("Doctors").document(userId).set(details, SetOptions.merge())
                .addOnSuccessListener(aVoid -> userLiveData.setValue(auth.getCurrentUser()))
                .addOnFailureListener(e -> authError.setValue(e.getMessage()));
    }

    // Save user data to Firestore
    private void saveUserToFirestore(String userId, String name, String email, String mobile, String gender) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("mobile", mobile);
        user.put("gender", gender);
        user.put("role", "patient");
        user.put("profileCompleted",false);
        user.put("bloodGroup",null);
        user.put("location",null);
        user.put("dob",null);
        user.put("profileImageUrl","");


        db.collection("Patients").document(userId).set(user)
                .addOnSuccessListener(aVoid -> userLiveData.setValue(auth.getCurrentUser()))
                .addOnFailureListener(e -> authError.setValue(e.getMessage()));
    }
    private void saveDoctorToFirestore(String userId, String name, String email, String mobile, String gender) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("mobile", mobile);
        user.put("gender", gender);
        user.put("role", "Doctor");
        user.put("profileCompleted",false);
        db.collection("Doctors").document(userId).set(user)
                .addOnSuccessListener(aVoid -> userLiveData.setValue(auth.getCurrentUser()))
                .addOnFailureListener(e -> authError.setValue(e.getMessage()));
    }

    // Getters for LiveData
    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<String> getAuthError() { return authError; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
}

