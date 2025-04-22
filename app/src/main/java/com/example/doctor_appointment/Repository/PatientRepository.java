package com.example.doctor_appointment.Repository;

import android.util.Log;

import com.example.doctor_appointment.Models.PatientModel;
import com.google.firebase.firestore.FirebaseFirestore;

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

                        Boolean profileCompletedBool = documentSnapshot.getBoolean("profileCompleted");
                        boolean profileCompleted = profileCompletedBool != null && profileCompletedBool;
                        if (name == null) name = "Unknown";

                        PatientModel patient = new PatientModel(
                                email, gender, mobile, name, profileCompleted, role, userIdFetched
                        );
                        Log.d("PatientDataDebug",""+patient.getName());

                        callback.onSuccess(patient);

                    } else {
                        callback.onFailure(new Exception("Patient data not found! Document is empty."));
                    }
                });
    }

}
