package com.example.doctor_appointment.Repository;

import android.util.Log;

import com.example.doctor_appointment.Models.PatientModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PatientRepository {

    private FirebaseFirestore db;

    public PatientRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface PatientDataCallback {
        void onSuccess(PatientModel patient);
        void onFailure(Exception e);
    }
    public void getPatientData(String userId, final PatientDataCallback callback) {
        db.collection("Patients")
                .document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        callback.onFailure(e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("PatientDataDebug","snapshot exist"+documentSnapshot);
                        String email = documentSnapshot.getString("email");
                        String gender = documentSnapshot.getString("gender");
                        String mobile = documentSnapshot.getString("mobile");
                        String name = documentSnapshot.getString("name");
                        String role = documentSnapshot.getString("role");
                        String userIdFetched = documentSnapshot.getString("userId");
                        String bloodGroup = documentSnapshot.getString("bloodGroup"); // May be null or missing
                        String location = documentSnapshot.getString("location"); // May be null or missing
                        String dob = documentSnapshot.getString("dob"); // May be null or missing
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        // Set default values if fields are missing
                        if(profileImageUrl == "") profileImageUrl = "unknown";
                        if (name == null) name = "Unknown";
                        if (bloodGroup == null) bloodGroup = "Not available"; // Default to empty string if not available
                        if (location == null) location = "Not available"; // Default to empty string if not available
                        if (dob == null) dob = "Not available"; // Default to empty string if not available
                        Boolean profileCompletedBool = documentSnapshot.getBoolean("profileCompleted");
                        boolean profileCompleted = profileCompletedBool != null && profileCompletedBool;
                        if (name == null) name = "Unknown";

                        PatientModel patient = new PatientModel(
                                email, gender, mobile, name, profileCompleted, role, userIdFetched, bloodGroup, location, dob ,profileImageUrl
                        );
                        Log.d("PatientDataDebug",""+patient.getName());

                        callback.onSuccess(patient);

                    } else {
                        callback.onFailure(new Exception("Patient data not found! Document is empty."));
                    }
                });
    }
    public void updatePatientData(String userId, String bloodGroup, String location, String dob,String name,String gender,String mobileNo, final UpdateCallback callback) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name",name);
        updatedData.put("gender",gender);
        updatedData.put("mobile",mobileNo);
        updatedData.put("bloodGroup", bloodGroup);
        updatedData.put("location", location);
        updatedData.put("dob", dob);
        updatedData.put("profileCompleted", true);

        db.collection("Patients")
                .document(userId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess();  // Notify success
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e);  // Notify failure
                });
    }
    public interface UpdateCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public static void updateUserProfilePhoto(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Patients")
                .document(userId)
                .update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Profile photo updated");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to update profile photo", e);
                });
    }
    public Task<String> getProfilePhotoUrl(String userId) {
        return db.collection("Patients").document(userId)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Return the URL from Firestore as a String
                            String profilePhotoUrl = document.getString("profileImageUrl");
                            return Tasks.forResult(profilePhotoUrl);
                        } else {
                            // No profile image found
                            return Tasks.forResult(null);
                        }
                    } else {
                        // Error fetching data
                        return Tasks.forException(task.getException());
                    }
                });
    }

}
