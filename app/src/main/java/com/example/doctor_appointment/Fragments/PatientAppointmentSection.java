package com.example.doctor_appointment.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctor_appointment.R;
import com.example.doctor_appointment.ViewModels.PatientAppointmentSectionViewModel;

public class PatientAppointmentSection extends Fragment {

    private PatientAppointmentSectionViewModel mViewModel;

    public static PatientAppointmentSection newInstance() {
        return new PatientAppointmentSection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_appointment_section, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PatientAppointmentSectionViewModel.class);
        // TODO: Use the ViewModel
    }

}