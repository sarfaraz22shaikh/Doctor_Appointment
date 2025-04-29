package com.example.doctor_appointment.Activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.doctor_appointment.Fragments.PatientAppointmentSection;
import com.example.doctor_appointment.Fragments.PatientHomeSection;
import com.example.doctor_appointment.Fragments.PatientProfileSection;
import com.example.doctor_appointment.Models.PatientModel;
import com.example.doctor_appointment.R;
import com.example.doctor_appointment.Repository.PatientRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientDashboard extends AppCompatActivity {
    private PatientRepository patientRepository;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        patientRepository = new PatientRepository();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadPatientData(currentUser.getUid());
        }

        initialize_views();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout1, new PatientHomeSection())
                .addToBackStack(null).commit();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PatientHomeSection();
                        break;
                    case 1:
                        fragment = new PatientAppointmentSection();
                        break;
                    case 2:
                        fragment = new PatientProfileSection();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout1, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initialize_views(){
        frameLayout = findViewById(R.id.framelayout1);
        tabLayout = findViewById(R.id.tablayout);
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
                                Intent intent = new Intent(PatientDashboard.this, PatientEditProfile.class);
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
