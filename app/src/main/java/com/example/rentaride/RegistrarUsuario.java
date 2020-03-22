package com.example.rentaride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nikartm.button.FitButton;


public class RegistrarUsuario extends AppCompatActivity {
    EditText textoemail, textocontraseña, textonombre;
    FitButton botonregistrar;
    String email, password, nombre;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        botonregistrar = findViewById(R.id.btregistrar);
        textonombre = findViewById(R.id.reg_nombre);
        textocontraseña = findViewById(R.id.reg_contraseña);
        textoemail = findViewById(R.id.reg_email);
        botonregistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registrar();
            }
        });

    }

    public void registrar() {
        if(validate()){
            Toast.makeText(getApplicationContext(), R.string.correctoregistro, Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegistrarUsuario.this, Login.class));
        }else
            Toast.makeText(getApplicationContext(), R.string.errorregistro, Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();
        nombre = textonombre.getText().toString();

        if (nombre.isEmpty()){
            textonombre.setError(getString(R.string.errornombre));
            valid = false;
        }

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
}
