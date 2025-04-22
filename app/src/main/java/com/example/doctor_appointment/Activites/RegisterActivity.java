package com.example.doctor_appointment.Activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.doctor_appointment.R;
import com.example.doctor_appointment.ViewModels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private String userType;
    private EditText fullname, emailInput, passwordInput, phonenumber;
    private Button registerButton;
    private RadioGroup genderGroup;
    private TextView title;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize_views();

        userType = getIntent().getStringExtra("USER_TYPE");

        setupUI(userType);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(this, "Registration Successful! Please Login.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, .class));
//                finish();
            }
        });

        // Observe errors
        registerViewModel.getAuthError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                registerButton.setEnabled(true);
            }
        });
        registerButton.setOnClickListener(v -> {
            String name = fullname.getText().toString().trim();
            String mobile = phonenumber.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String gender = getSelectedGender();

            if (userType != null) {
                if (userType.equals("doctor")) {
                    registerViewModel.registerDoctor(name,email,mobile,password,gender);
                    registerButton.setEnabled(false);
                }
                else{
                    registerViewModel.registerPatient(name, email, mobile, password, gender);
                    registerButton.setEnabled(false);
                }
            }
        });
    }
    private void initialize_views(){
        fullname = findViewById(R.id.fullname);
        phonenumber = findViewById(R.id.phonenumber);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.register);
        genderGroup = findViewById(R.id.gender_group);
        title = findViewById(R.id.textView2);
    }
    private void setupUI(String userType) {
        if (userType != null) {
            if (userType.equals("doctor")) {
                // Adjust UI for Doctor
                title.setText(R.string.doctor_registration);
            }
        }
    }
    private String getSelectedGender() {
        int selectedId = genderGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedId);
        return selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";
    }
}
