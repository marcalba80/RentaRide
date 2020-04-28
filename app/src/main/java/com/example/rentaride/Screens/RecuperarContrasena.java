package com.example.rentaride.Screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaride.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RecuperarContrasena extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
    }

    public void recuperarLogin(View view) {
        EditText e = findViewById(R.id.rec_email);
        String email = e.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e.setError(getString(R.string.email));
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email);
            Toast.makeText(getApplicationContext(), "Se ha enviado el correo de recuperación! Revise su buzón.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
