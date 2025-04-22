package com.example.doctor_appointment.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.doctor_appointment.Models.PatientModel;

public class SessionManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "user_session";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_NAME = "name";
    private static final String KEY_PROFILE_COMPLETED = "profileCompleted";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USERID = "userId";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(PatientModel patient) {
        editor.putString(KEY_EMAIL, patient.getEmail());
        editor.putString(KEY_GENDER, patient.getGender());
        editor.putString(KEY_MOBILE, patient.getMobile());
        editor.putString(KEY_NAME, patient.getName());
        editor.putBoolean(KEY_PROFILE_COMPLETED, patient.isProfileCompleted());
        editor.putString(KEY_ROLE, patient.getRole());
        editor.putString(KEY_USERID, patient.getUserId());
        editor.apply();
    }

    public PatientModel getUser() {
        PatientModel patient = new PatientModel();
        patient.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
        patient.setGender(sharedPreferences.getString(KEY_GENDER, ""));
        patient.setMobile(sharedPreferences.getString(KEY_MOBILE, ""));
        patient.setName(sharedPreferences.getString(KEY_NAME, ""));
        patient.setProfileCompleted(sharedPreferences.getBoolean(KEY_PROFILE_COMPLETED, false));
        patient.setRole(sharedPreferences.getString(KEY_ROLE, ""));
        patient.setUserId(sharedPreferences.getString(KEY_USERID, ""));
        return patient;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
