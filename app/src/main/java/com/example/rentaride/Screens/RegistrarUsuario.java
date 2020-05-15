package com.example.rentaride.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaride.R;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;


public class RegistrarUsuario extends AppCompatActivity {
    EditText textoemail, textocontraseña, textonombre, textotelefono;
    FitButton botonregistrar;
    String email, password, nombre, telefono;
    SharedPreferences mPreference;
    private KProgressHUD k;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        botonregistrar = findViewById(R.id.btregistrar);
        textonombre = findViewById(R.id.reg_nombre);
        textocontraseña = findViewById(R.id.reg_contraseña);
        textoemail = findViewById(R.id.reg_email);
        textotelefono = findViewById(R.id.telefono);
        mPreference = PreferenceManager.getDefaultSharedPreferences(RegistrarUsuario.this);
        botonregistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registrar();
            }
        });

    }

    public void registrar() {
        k = KProgressHUD.create(RegistrarUsuario.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Creando usuario...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        if(validate()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nombre)
                                        .build();
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    HashMap<String, String> hm = new HashMap<>();
                                                    hm.put("Telefono", telefono);
                                                    FirebaseFirestore.getInstance().collection("Datos").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .set(hm);
                                                    Toast.makeText(getApplicationContext(), R.string.correctoregistro, Toast.LENGTH_LONG).show();
                                                    mEditor = mPreference.edit();
                                                    mEditor.putString(getString(R.string.preferenceEmail), textoemail.getText().toString());
                                                    mEditor.apply();
                                                    mEditor.putString(getString(R.string.preftelefono), textotelefono.getText().toString());
                                                    mEditor.apply();
                                                    mEditor.putString(getString(R.string.preferenceUsername), textonombre.getText().toString());
                                                    mEditor.apply();
                                                    k.dismiss();
                                                    startActivity(new Intent(RegistrarUsuario.this, Login.class));
                                                }else {
                                                    k.dismiss();
                                                    Toast.makeText(getApplicationContext(), R.string.errorregistro, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                k.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.errorregistro, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(), R.string.errorregistro, Toast.LENGTH_LONG).show();
            k.dismiss();
        }

    }

    public boolean validate() {

        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();
        nombre = textonombre.getText().toString();
        telefono = textotelefono.getText().toString();

        if (nombre.isEmpty()){
            textonombre.setError(getString(R.string.errornombre));
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textoemail.setError(getString(R.string.email));
            return false;
        }

        if (telefono.isEmpty() || telefono.length() < 9) {
            textotelefono.setError(getString(R.string.telef));
            return false;
        }

        if (password.isEmpty() || password.length() < 6 ) {
            textocontraseña.setError(getString(R.string.errorcontraseña));
            return false;
        }

        return true;
    }

}
