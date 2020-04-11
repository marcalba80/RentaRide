package com.example.rentaride.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.rentaride.R;


public class PerfilFragment extends Fragment {


    public PerfilFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_config, container, false);
        getFragmentManager().beginTransaction().replace(R.id.pref_content, new PrefsFragment()).commit();

        return v;
    }
    public static class PrefsFragment extends PreferenceFragmentCompat {

        SharedPreferences mPreference;

        EditTextPreference emailEditText, usernameEditText, numberEditText;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.configscreen);

            mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

            emailEditText = (EditTextPreference) findPreference(getResources().getString(R.string.preferenceEmail));
            usernameEditText = (EditTextPreference) findPreference(getResources().getString(R.string.preferenceUsername));
            numberEditText = (EditTextPreference) findPreference(getResources().getString(R.string.preftelefono));

            checkSharedPreferences();
            usernameEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String val = (String) newValue;
                    SharedPreferences.Editor mEdit = mPreference.edit();
                    mEdit.putString(getString(R.string.preferenceUsername), val);
                    mEdit.apply();
                    usernameEditText.setTitle(getResources().getString(R.string.literalUsername) + " " + val);
                    return true;
                }
            });

            numberEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String val = (String) newValue;
                    SharedPreferences.Editor mEdit = mPreference.edit();
                    mEdit.putString(getString(R.string.preftelefono), val);
                    mEdit.apply();
                    numberEditText.setTitle(getResources().getString(R.string.literalTelefono) + " " + val);
                    return true;
                }
            });
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }

        private void checkSharedPreferences() {
            String email = mPreference.getString(getString(R.string.preferenceEmail), "");
            String username = mPreference.getString(getString(R.string.preferenceUsername), "");
            String number = mPreference.getString(getString(R.string.preftelefono), "");


            emailEditText.setTitle(getResources().getString(R.string.literalCorreo) + " " + email);
            usernameEditText.setTitle(getResources().getString(R.string.literalUsername) + " " + username);
            numberEditText.setTitle(getResources().getString(R.string.literalTelefono) + " " + number);
            usernameEditText.setText(username);
            numberEditText.setText(number);

        }
    }
}
