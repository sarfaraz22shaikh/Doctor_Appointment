package com.example.doctor_appointment.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctor_appointment.R;

public class MainActivity extends AppCompatActivity {
    private Button optionDoctor;
    private Button optionPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initialize_view();
        optionDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("USER_TYPE", "doctor");
            startActivity(intent);
        });
        optionPatient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("USER_TYPE", "patient");
            startActivity(intent);
        });
    }
    private void initialize_view(){
        optionDoctor = findViewById(R.id.optionDoctor);
        optionPatient = findViewById(R.id.optionPatient);
    }
}