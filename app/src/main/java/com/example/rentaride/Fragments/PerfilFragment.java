package com.example.rentaride.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEvento;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Tarjeta;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Screens.Login;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.manojbhadane.PaymentCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        EditTextPreference emailEditText, usernameEditText, numberEditText;
        ListPreference listPreference;
        Preference tarjeta;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.configscreen);
            mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            emailEditText = findPreference(getResources().getString(R.string.preferenceEmail));
            usernameEditText = findPreference(getResources().getString(R.string.preferenceUsername));
            numberEditText = findPreference(getResources().getString(R.string.preftelefono)); 
            listPreference = findPreference(getString(R.string.preferenciared));
            tarjeta = findPreference(getResources().getString(R.string.preferenceTarjeta));

            tarjeta.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.popup_tarjeta);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.transp);
                    dialog.show();
                    PaymentCardView paymentCard = (PaymentCardView)dialog.findViewById(R.id.creditCard);
                    paymentCard.setOnPaymentCardEventListener(new PaymentCardView.OnPaymentCardEventListener() {
                        @Override
                        public void onCardDetailsSubmit(String month, String year, String cardNumber, String cvv) {
                            SharedPreferences.Editor editor = mPreference.edit();
                            editor.putString("month", month);
                            editor.putString("year", year);
                            editor.putString("cardNumber", cardNumber);
                            editor.putString("cvv", cvv);
                            editor.apply();
                            Toast.makeText(getContext(), "Se ha guardado la tarjeta correctamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog.hide();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getActivity(),"Datos incorrectos", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                    return false;
                }
            });
            checkSharedPreferences();
            usernameEditText.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName((String) newValue)
                            .build();
                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                    usernameEditText.setTitle(getResources().getString(R.string.literalUsername) + " " + newValue);
                    SharedPreferences.Editor mEdit = mPreference.edit();
                    mEdit.putString(getString(R.string.preferenceUsername), (String) newValue);
                    mEdit.apply();
                    return false;
                }
            });
            numberEditText.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("Telefono", (String) newValue);
                    FirebaseFirestore.getInstance().collection("Datos").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(hm);
                    numberEditText.setTitle(getResources().getString(R.string.literalTelefono) + " " + newValue);
                    SharedPreferences.Editor mEdit = mPreference.edit();
                    mEdit.putString(getString(R.string.preftelefono), (String) newValue);
                    mEdit.apply();
                    return false;
                }
            });
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    mPreference.edit().putString(getString(R.string.preferenciared), (String) newValue).apply();
                    return false;
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
