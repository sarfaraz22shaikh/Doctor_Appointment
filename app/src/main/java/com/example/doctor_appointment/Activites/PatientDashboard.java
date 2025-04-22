package com.example.doctor_appointment.Activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctor_appointment.Models.PatientModel;
import com.example.doctor_appointment.R;
import com.example.doctor_appointment.Repository.PatientRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientDashboard extends AppCompatActivity {
    private PatientRepository patientRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        patientRepository = new PatientRepository();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadPatientData(currentUser.getUid());
        }

    }
    private void loadPatientData(String userId) {
        Log.d("PatientDataDebug", "Fetching patient data for userId: " + userId);

        patientRepository.getPatientData(userId, new PatientRepository.PatientDataCallback() {
            @Override
            public void onSuccess(PatientModel patient) {

                if (patient == null) {
                    Log.e("PatientDataDebug", "PatientModel is NULL! Check Firestore document structure.");
                    Toast.makeText(PatientDashboard.this, "Error: User data not found.", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d("PatientDataDebug", "Patient data loaded: " + patient.getName()
                        + ", ProfileCompleted: " + patient.isProfileCompleted());

                if (!patient.isProfileCompleted()) {
                    Log.d("PatientDataDebug", "Profile not completed. Showing alert...");

                    new AlertDialog.Builder(PatientDashboard.this)
                            .setTitle("Complete Your Profile")
                            .setMessage("Your profile is incomplete. Please complete it to continue using the app properly.")
                            .setCancelable(false)
                            .setPositiveButton("Complete Now", (dialog, which) -> {
                                Intent intent = new Intent(PatientDashboard.this, PatientProfile.class);
                                startActivity(intent);
                                // Close the dashboard after redirect
                            })
                            .setNegativeButton("Later", (dialog, which) -> {
                                dialog.dismiss(); // Let user stay, but you can limit features if needed
                            })
                            .show();

                } else {
                    Log.d("PatientDataDebug", "Profile is completed. Staying on Dashboard.");
                    Toast.makeText(PatientDashboard.this, "Welcome " + patient.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PatientDataDebug", "Error fetching patient data: ", e);
                Toast.makeText(PatientDashboard.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
