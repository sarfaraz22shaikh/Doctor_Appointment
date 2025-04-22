package com.example.doctor_appointment.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctor_appointment.R;
import com.example.doctor_appointment.Repository.AuthRepository;

public class LoginActivity extends AppCompatActivity {

    private TextView textTitle;
    private ImageView imageView,imageView3,imageView4;
    private EditText editEmail, editPassword;
    private Button btnSubmit,btnRegister;
    private String userType;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        initialize_views();

        // Get the user type from the Intent
        userType = getIntent().getStringExtra("USER_TYPE");

        // Set up the UI based on the user type (doctor or patient)
        setupUI(userType);


        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
        });
        // Button to submit login/register logic
        authRepository = new AuthRepository();
        btnSubmit.setOnClickListener(v -> {
            // Handle login/register logic based on user type (doctor or patient)
            if (userType != null) {
                if (userType.equals("doctor")) {
                    String email = editEmail.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    authRepository.loginUser(email,password,LoginActivity.this);
                } else {
                    // Patient login/register logic
                    String email = editEmail.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    authRepository.loginUser(email,password,LoginActivity.this);
                }
            }
        });
    }
    private void initialize_views(){
        textTitle = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        editEmail = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        btnSubmit = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.DocRegister);
    }

    private void setupUI(String userType) {
        if (userType != null) {
            if (userType.equals("patient")) {
                // Adjust UI for Doctor
                textTitle.setText(R.string.Patient);
                textTitle.setTextColor(Color.parseColor("#38B0F5"));
                imageView.setImageResource(R.drawable.heart_attack__1_);
                imageView3.setImageResource(R.drawable.stethoscope__1_);
                imageView4.setImageResource(R.drawable.half_circle1);
                editEmail.setBackgroundResource(R.drawable.button_border1);
                editPassword.setBackgroundResource(R.drawable.button_border1);
                btnSubmit.setBackgroundResource(R.drawable.button_border_extra);
            }
            else{
                btnSubmit.setBackgroundResource(R.drawable.button_border_extra1);
            }
        }
    }
}
