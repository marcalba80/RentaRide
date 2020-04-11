package com.example.rentaride.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaride.R;


public class RecuperarContrasena extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
    }

    public void recuperar(View view) {
        EditText e = findViewById(R.id.rec_email);
        String email = e.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e.setError(getString(R.string.email));
        } else {
            Toast.makeText(getApplicationContext(), R.string.correo_recuperacion, Toast.LENGTH_LONG).show();
            startActivity(new Intent(RecuperarContrasena.this, Login.class));
        }
    }
}
