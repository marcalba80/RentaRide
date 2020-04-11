package com.example.rentaride.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEvento;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.rentaride.Utils.Utils.biciprueba;
import static com.example.rentaride.Utils.Utils.cocheprueba;
import static com.example.rentaride.Utils.Utils.motoprueba;

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
        getFragmentManager().beginTransaction().replace(R.id.pref_content,new PrefsFragment()).commit();

        return v;
    }
    public static class PrefsFragment extends PreferenceFragmentCompat {

        SharedPreferences mPreference;

        androidx.preference.EditTextPreference emailEditText, usernameEditText, numberEditText;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.configscreen);

            mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

            emailEditText = (androidx.preference.EditTextPreference) findPreference(getResources().getString(R.string.preferenceEmail));
            usernameEditText = (androidx.preference.EditTextPreference) findPreference(getResources().getString(R.string.preferenceUsername));
            numberEditText = (androidx.preference.EditTextPreference) findPreference(getResources().getString(R.string.preftelefono));

            checkSharedPreferences();
            usernameEditText.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {
                    final String val = (String) newValue;
                    SharedPreferences.Editor mEdit = mPreference.edit();
                    mEdit.putString(getString(R.string.preferenceUsername), val);
                    mEdit.apply();
                    usernameEditText.setTitle(getResources().getString(R.string.literalUsername) + " " + val);
                    return true;
                }
            });

            numberEditText.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {
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
