package com.example.rentaride.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaride.R;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;


public class Login extends AppCompatActivity {
    EditText textoemail, textocontraseña;
    CircularProgressButton botonlogin;
    String email, password;
    SharedPreferences pref;

    SharedPreferences mPreference;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        botonlogin = findViewById(R.id.login);
        textocontraseña = findViewById(R.id.input_password);
        textoemail = findViewById(R.id.input_email);
        pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
        if(pref.getBoolean(getString(R.string.mantenersesion),false)){
            startActivity(new Intent(Login.this, Main.class));
            finish();
        }else
            botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if(validate()){
            botonlogin.showProgress();
            botonlogin.setIndeterminateProgressMode(true);
            botonlogin.showComplete();
            SharedPreferences.Editor mEditor = mPreference.edit();
            mEditor.putBoolean(getString(R.string.mantenersesion), true);
            mEditor.apply();
            Toast.makeText(getApplicationContext(), R.string.correctologin, Toast.LENGTH_LONG).show();
            startActivity(new Intent(Login.this, Main.class));
            finish();
        }else
            Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
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

        if(email.equals(pref.getString(getString(R.string.preferenceEmail), "")) &&  password.equals(pref.getString(getString(R.string.preferencePass), "")))
        return true;
        return false;
    }

    public void olvidado(View view) {
        startActivity(new Intent(Login.this, RecuperarContrasena.class));
    }

    public void registro(View view) {
        startActivity(new Intent(Login.this, RegistrarUsuario.class));
    }
}
