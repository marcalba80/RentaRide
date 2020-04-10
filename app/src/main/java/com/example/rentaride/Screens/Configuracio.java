package com.example.rentaride.Screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rentaride.R;

public class Configuracio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configuración");

        getFragmentManager().beginTransaction().replace(R.id.pref_content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        SharedPreferences mPreference;
        SharedPreferences.Editor mEditor;

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