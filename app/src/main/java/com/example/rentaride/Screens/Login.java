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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;


public class Login extends AppCompatActivity {
    EditText textoemail, textocontraseña;
    CircularProgressButton botonlogin;
    String email, password;
    SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        botonlogin = findViewById(R.id.login);
        textocontraseña = findViewById(R.id.input_password);
        textoemail = findViewById(R.id.input_email);
        pref = PreferenceManager.getDefaultSharedPreferences(Login.this);

        if(pref.getBoolean(getString(R.string.mantenersesion),false)){
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(Login.this, Main.class));
                finish();
            }
        }

        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if(validate()){
            botonlogin.showProgress();
            botonlogin.setIndeterminateProgressMode(true);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setData();
                            }
                            else {
                                botonlogin.showError();
                                Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                botonlogin.showIdle();
                            }
                        }
                    });
        }else
            Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
    }

    private void setData() {
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor mEdit = mPreference.edit();
        mEdit.putString(getString(R.string.preferenceEmail), FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mEdit.putString(getString(R.string.preferenceUsername), FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        FirebaseFirestore.getInstance().collection("Datos").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot d = task.getResult();
                    mEdit.putString(getString(R.string.preftelefono), d.getString("Telefono"));
                    mEdit.apply();
                    botonlogin.showComplete();
                    Toast.makeText(getApplicationContext(), R.string.correctologin, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, Main.class));
                    finish();
                }else{
                    botonlogin.showError();
                    Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    botonlogin.showIdle();
                }
            }
        });
    }

    public boolean validate() {
        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textoemail.setError(getString(R.string.email));
            return false;
        }

        if (password.isEmpty() || password.length() < 6 ) {
            textocontraseña.setError(getString(R.string.errorcontraseña));
            return false;
        }
        return true;
    }

    public void olvidado(View view) {
        startActivity(new Intent(Login.this, RecuperarContrasena.class));
    }

    public void registro(View view) {
        startActivity(new Intent(Login.this, RegistrarUsuario.class));
    }
}
