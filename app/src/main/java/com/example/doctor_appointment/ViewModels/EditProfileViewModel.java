package com.example.doctor_appointment.ViewModels;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

import com.example.doctor_appointment.Models.PatientModel;
import com.example.doctor_appointment.Repository.CloudinaryRepository;
import com.example.doctor_appointment.Repository.PatientRepository;

public class EditProfileViewModel extends ViewModel {
    private final CloudinaryRepository cloudinaryRepository;
    private final MutableLiveData<Boolean> uploading = new MutableLiveData<>();
    private final MutableLiveData<String> uploadSuccessUrl = new MutableLiveData<>();
    private final MutableLiveData<String> uploadError = new MutableLiveData<>();
    private MutableLiveData<PatientModel> patientDataLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> profileCompletedLiveData = new MutableLiveData<>();
    private MutableLiveData<Exception> errorLiveData = new MutableLiveData<>();
    private PatientRepository patientRepository;
    private MutableLiveData<Boolean> updateSuccessLiveData = new MutableLiveData<>();
    public EditProfileViewModel() {
        cloudinaryRepository = new CloudinaryRepository();
        patientRepository = new PatientRepository(); // Initialize repository
    }

    // Fetch patient data from Firebase via PatientRepository
    public void fetchPatientData(String userId) {
        patientRepository.getPatientData(userId, new PatientRepository.PatientDataCallback() {
            @Override
            public void onSuccess(PatientModel patient) {
                patientDataLiveData.setValue(patient);  // Set patient data in LiveData
                profileCompletedLiveData.setValue(patient.isProfileCompleted());// Set profile completion status
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.setValue(e); // Pass any errors to the UI
            }
        });
    }
    public void updatePatientData(String userId, String bloodGroup, String location, String dob,String name,String gender,String mobileNo) {
        patientRepository.updatePatientData(userId, bloodGroup, location, dob,name,gender,mobileNo, new PatientRepository.UpdateCallback() {
            @Override
            public void onSuccess() {
                // Notify the UI that the data was successfully updated
                updateSuccessLiveData.setValue(true);

            }

            @Override
            public void onFailure(Exception e) {
                // Notify the UI of the error
                updateSuccessLiveData.setValue(true);
            }
        });
    }
    // Get LiveData for patient data to observe in the UI
    public LiveData<PatientModel> getPatientData() {
        return patientDataLiveData;
    }

    // Get LiveData for profile completion status
    public LiveData<Boolean> isProfileCompleted() {
        return profileCompletedLiveData;
    }

    // Get LiveData for error messages
    public LiveData<Exception> getError() {
        return errorLiveData;
    }
    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccessLiveData;
    }
    public LiveData<Boolean> getUploading() { return uploading; }
    public LiveData<String> getUploadSuccessUrl() { return uploadSuccessUrl; }
    public LiveData<String> getUploadError() { return uploadError; }

    public  void uploadProfileImage(Uri imageUri, Context context) {
        uploading.setValue(true);
        cloudinaryRepository.uploadImage(imageUri, context)
                .addOnSuccessListener(url -> {
                    uploading.setValue(false);
                    uploadSuccessUrl.setValue(url);
                    PatientRepository.updateUserProfilePhoto(url); // ✅ correct
                })
                .addOnFailureListener(e -> {
                    uploading.setValue(false);
                    uploadError.setValue(e.getMessage());
                });
    }

}
