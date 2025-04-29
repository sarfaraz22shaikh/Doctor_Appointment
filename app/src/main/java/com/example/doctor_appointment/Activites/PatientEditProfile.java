package com.example.doctor_appointment.Activites;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.doctor_appointment.R;
import com.example.doctor_appointment.Repository.PatientRepository;
import com.example.doctor_appointment.ViewModels.EditProfileViewModel;
import com.example.doctor_appointment.databinding.ActivityPatientEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Calendar;

public class PatientEditProfile extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private ActivityPatientEditProfileBinding binding;
    private PatientRepository patientRepository;
    private EditProfileViewModel editProfileViewModel;
    private TextView emailTextView;
    private TextView dobTextView;
    private TextView editLocation;
    private TextView editName;
    private TextView mobileEditText;
    private Spinner genderSpinner;
    private Spinner bloodGroupSpinner;
    private Button saveButton;
    private ImageView imageViewProfile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                editProfileViewModel.uploadProfileImage(selectedImageUri, this);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

        binding = ActivityPatientEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialze_views();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        editProfileViewModel.fetchPatientData(userId);
        patientRepository = new PatientRepository();

        editProfileViewModel.getPatientData().observe(this, patient -> {
            if (patient != null) {
                // Set data in UI
                editName.setText(patient.getName());
                emailTextView.setText(patient.getEmail());
                mobileEditText.setText(patient.getMobile());
                setGenderInSpinner(patient.getGender());
                dobTextView.setText(patient.getDob());
                editLocation.setText(patient.getLocation());
                setBloodGroupInSpinner(patient.getBloodGroup());
            }
        });
        editProfileViewModel.getError().observe(this, e -> {
            // Handle error (e.g., show Toast message)
            if (e != null) {
                Toast.makeText(PatientEditProfile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        editProfileViewModel.getUpdateSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                // Show success toast if update was successful
                Toast.makeText(PatientEditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Show error toast if update failed
                Toast.makeText(PatientEditProfile.this, "Error updating profile!", Toast.LENGTH_SHORT).show();
            }
        });
        editProfileViewModel.getUpdateSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                // Show success toast if update was successful
                Toast.makeText(PatientEditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                // Navigate to PatientDashboard
                Intent intent = new Intent(PatientEditProfile.this, PatientDashboard.class);
                startActivity(intent);
                finish();  // Optional: finish the current activity if you don't want it in the backstack
            } else {
                // Show error toast if update failed
                Toast.makeText(PatientEditProfile.this, "Error updating profile!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.profilePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
        binding.profilePhoto.setOnLongClickListener(view -> {
            // This is where we handle the long press
            showProfilePhotoPreview();
            return true; // Return true to indicate the long click was handled
        });

        editProfileViewModel.getUploading().observe(this, uploading -> {
            if (uploading) {
                // Show Progress Dialog
            } else {
                // Hide Progress Dialog
            }
        });

        editProfileViewModel.getUploadSuccessUrl().observe(this, imageUrl -> {
            if (imageUrl != null) {
                Glide.with(this).load(imageUrl).into(binding.profilePhoto);
                Toast.makeText(this, "Profile photo uploaded!", Toast.LENGTH_SHORT).show();
            }
        });

        editProfileViewModel.getUploadError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Upload Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        mobileEditText.setOnClickListener(v -> {
            showCenterEditPopup("Mobile", mobileEditText.getText().toString(), newValue -> {
                mobileEditText.setText(newValue);
            });
        });
        editName.setOnClickListener(v -> {
            showCenterEditPopup("Name", editName.getText().toString(), newValue -> {
                editName.setText(newValue);
            });
        });
        editLocation.setOnClickListener(v -> {
            showCenterEditPopup("Location", editLocation.getText().toString(), newValue -> {
                editLocation.setText(newValue);
            });
        });

        dobTextView.setOnClickListener(v -> {
            showDatePicker(date -> {
                dobTextView.setText(date);
            });
        });
        saveButton.setOnClickListener(v -> {
            // Get selected blood group from Spinner
            String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
            String location = editLocation.getText().toString();
            String dob = dobTextView.getText().toString();
            String name = editName.getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();
            String mobileNo = mobileEditText.getText().toString();
            // Validate inputs (optional, you can add more validation)
            if (!bloodGroup.isEmpty() && !location.isEmpty() && !dob.isEmpty()) {
                // Update the profile in ViewModel
                editProfileViewModel.updatePatientData(userId, bloodGroup, location, dob,name,gender,mobileNo);
            } else {
                // Show error message if fields are empty
                Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
            }
        });
        observeViewModel();
        loadProfileImage();
    }

    private void initialze_views() {
        imageViewProfile = findViewById(R.id.profilePhoto);
        emailTextView = findViewById(R.id.emailTextView);
        dobTextView = findViewById(R.id.dobTextView);
        editLocation = findViewById(R.id.editLocation);
        editName = findViewById(R.id.editName);
        mobileEditText = findViewById(R.id.editMobile);
        genderSpinner = findViewById(R.id.genderSpinner);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        saveButton = findViewById(R.id.saveButton);
    }
    private void showProfilePhotoPreview() {
        // Show the profile photo preview in a larger size, like a dialog or fullscreen
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        patientRepository.getProfilePhotoUrl(userId)
                .addOnSuccessListener(profilePhotoUrl -> {
                    if (profilePhotoUrl != null) {
                        // Open the image in a full-screen dialog or activity
                        showPreviewDialog(profilePhotoUrl);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("EditProfileActivity", "Error fetching profile photo for preview", e);
                });
    }
    private void showPreviewDialog(String profilePhotoUrl) {
        Log.d("ProfilePreview", "Profile photo URL: " + profilePhotoUrl);

        Dialog previewDialog = new Dialog(this);
        previewDialog.setContentView(R.layout.dialog_image_preview);
        ImageView imageViewPreview = previewDialog.findViewById(R.id.imageViewPreview);

        // Use Glide to load the image into the dialog
        if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
            Glide.with(this)
                    .load(profilePhotoUrl)
                    .placeholder(R.drawable.oq6utw0)  // Show a placeholder while loading
                    .error(R.drawable._990224)// Show error image if loading fails
                    .override(500, 500)
                    .centerCrop()  // Scale image properly
                    .into(imageViewPreview);
        } else {
            Glide.with(this)
                    .load(R.drawable.oq6utw0)  // Fallback placeholder if URL is empty
                    .into(imageViewPreview);
        }

        previewDialog.show();
    }

    private void observeViewModel() {
        editProfileViewModel.getUploadSuccessUrl().observe(this, imageUrl -> {
            // After uploading, update the ImageView with new profile picture
            Glide.with(this).load(imageUrl).into(binding.profilePhoto);
        });
    }
    private void showCenterEditPopup(String title, String currentValue, OnValueSubmittedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_edit_text, null);

        EditText editTextField = view.findViewById(R.id.editTextField);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        editTextField.setHint("Enter new " + title);
        editTextField.setText(currentValue);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        dialog.show();

        btnSubmit.setOnClickListener(v -> {
            String newValue = editTextField.getText().toString().trim();
            if (!newValue.isEmpty()) {
                listener.onValueSubmitted(newValue);
                dialog.dismiss();
            } else {
                editTextField.setError("Please enter " + title);
            }
        });
    }

    // Interface to send the value back
    public interface OnValueSubmittedListener {
        void onValueSubmitted(String newValue);
    }

    private void showDatePicker(OnDateSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format date as you like
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    listener.onDateSelected(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        Calendar minDate = Calendar.getInstance();
        minDate.set(1900, 0, 1); // January 1, 1900
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }

    // Interface to send selected date back
    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    private void setGenderInSpinner(String gender) {
        if (gender != null) {
            // Check which gender option matches the fetched gender
            switch (gender) {
                case "Male":
                    genderSpinner.setSelection(1); // Set "Male" in spinner
                    break;
                case "Female":
                    genderSpinner.setSelection(2); // Set "Female" in spinner
                    break;
                case "Other":
                    genderSpinner.setSelection(3); // Set "Other" in spinner
                    break;
                default:
                    genderSpinner.setSelection(0); // Default to "Male" if no match
                    break;
            }
        }
    }
    private void setBloodGroupInSpinner(String bloodGroup) {
        if (bloodGroup != null) {
            // Check which blood group option matches the fetched blood group
            switch (bloodGroup) {
                case "A+":
                    bloodGroupSpinner.setSelection(1); // Set "A+" in spinner
                    break;
                case "A-":
                    bloodGroupSpinner.setSelection(2); // Set "A-" in spinner
                    break;
                case "B+":
                    bloodGroupSpinner.setSelection(3); // Set "B+" in spinner
                    break;
                case "B-":
                    bloodGroupSpinner.setSelection(4); // Set "B-" in spinner
                    break;
                case "AB+":
                    bloodGroupSpinner.setSelection(5); // Set "AB+" in spinner
                    break;
                case "AB-":
                    bloodGroupSpinner.setSelection(6); // Set "AB-" in spinner
                    break;
                case "O+":
                    bloodGroupSpinner.setSelection(7); // Set "O+" in spinner
                    break;
                case "O-":
                    bloodGroupSpinner.setSelection(8); // Set "O-" in spinner
                    break;
                default:
                    bloodGroupSpinner.setSelection(0); // Default to "Select Blood Group" if no match
                    break;
            }
        } else {
            // If bloodGroup is null, set it to the default "Select Blood Group"
            bloodGroupSpinner.setSelection(0);
        }
    }
    private void loadProfileImage() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Fetch profile photo URL from the repository
        patientRepository.getProfilePhotoUrl(userId)
                .addOnSuccessListener(profilePhotoUrl -> {
                    if (profilePhotoUrl != null) {
                        // Load the profile photo URL using Glide
                        Glide.with(this)
                                .load(profilePhotoUrl)
                                .placeholder(R.drawable.oq6utw0)  // Optional: Add placeholder image
                                .into(binding.profilePhoto);
                    } else {
                        // Handle case where there is no photo (optional)
                        Glide.with(this)
                                .load(R.drawable.oq6utw0)
                                .into(binding.profilePhoto);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("EditProfileActivity", "Error fetching profile photo URL", e);
                });
    }

}
