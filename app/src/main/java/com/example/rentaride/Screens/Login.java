package com.example.rentaride.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaride.R;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;


public class Login extends AppCompatActivity {
    EditText textoemail, textocontraseña;
    CircularProgressButton botonlogin;
    String email, password;

    SharedPreferences mPreference;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        botonlogin = findViewById(R.id.login);
        textocontraseña = findViewById(R.id.input_password);
        textoemail = findViewById(R.id.input_email);

        //mPreference = getSharedPreferences(getString(R.string.configuration), Context.MODE_PRIVATE);
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        checkSharedPreferences();

        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void checkSharedPreferences() {
        String email = mPreference.getString(getString(R.string.preferenceEmail), "");
        String pass = mPreference.getString(getString(R.string.preferencePass), "");

        textoemail.setText(email);
        textocontraseña.setText(pass);
    }

    public void login() {
        if(validate()){
            botonlogin.showProgress();
            botonlogin.setIndeterminateProgressMode(true);
            botonlogin.showComplete();
            Toast.makeText(getApplicationContext(), R.string.correctologin, Toast.LENGTH_LONG).show();
            startActivity(new Intent(Login.this, Calendar.class));
            finish();
        }else
            Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();

        //startActivity(new Intent(Login.this, Calendar.class));
        mEditor = mPreference.edit();
        mEditor.putString(getString(R.string.preferenceEmail), textoemail.getText().toString());
        mEditor.apply();
        mEditor.putString(getString(R.string.preferencePass), textocontraseña.getText().toString());
        mEditor.apply();
        mEditor.putString(getString(R.string.preferenceUsername), "marc");
        mEditor.apply();

        startActivity(new Intent(Login.this, Main.class));
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textoemail.setError(getString(R.string.email));
            valid = false;
        }

        if (password.isEmpty() || password.length() < 6 ) {
            textocontraseña.setError(getString(R.string.errorcontraseña));
            valid = false;
        }
        return valid;
    }

    public void olvidado(View view) {
        startActivity(new Intent(Login.this, RecuperarContrasena.class));
    }

    public void registro(View view) {
        startActivity(new Intent(Login.this, RegistrarUsuario.class));
    }
}
