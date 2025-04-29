package com.example.doctor_appointment.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.doctor_appointment.Activites.PatientEditProfile;
import com.example.doctor_appointment.R;
import com.example.doctor_appointment.ViewModels.PatientProfileSectionViewModel;

public class PatientProfileSection extends Fragment {

    private PatientProfileSectionViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_profile_section, container, false);
        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PatientEditProfile.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PatientProfileSectionViewModel.class);
        // TODO: Use the ViewModel
    }

}