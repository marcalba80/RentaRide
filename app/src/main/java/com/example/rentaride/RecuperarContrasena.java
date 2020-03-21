package com.example.rentaride;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasena extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
    }

    public void recuperar(View view) {
        EditText e = findViewById(R.id.rec_email);
        FirebaseAuth.getInstance().sendPasswordResetEmail(e.getText().toString());
        Toast.makeText(getApplicationContext(), "Se ha enviado el correo de recuperación! Revise su buzón.", Toast.LENGTH_LONG).show();
        finish();
    }
}
