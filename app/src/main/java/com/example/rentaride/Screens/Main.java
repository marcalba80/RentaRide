package com.example.rentaride.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.rentaride.Fragments.MisReservasFragment;
import com.example.rentaride.Fragments.OfertaFragment;
import com.example.rentaride.Fragments.PerfilFragment;
import com.example.rentaride.Fragments.ReservaFragment;
import com.example.rentaride.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreference = PreferenceManager.getDefaultSharedPreferences(Main.this);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mires:
                        abrirFragment(new MisReservasFragment());
                        return true;
                    case R.id.reservar:
                        abrirFragment(new ReservaFragment());
                        return true;
                    case R.id.publicar:
                        abrirFragment(new OfertaFragment());
                        return true;
                    case R.id.perfil:
                        abrirFragment(new PerfilFragment());
                        return true;
                }
                return false;
            }
        });
        if(savedInstanceState != null) bottomNavigation.setSelectedItemId(savedInstanceState.getInt(getString(R.string.fragment), 0));
        else bottomNavigation.setSelectedItemId(R.id.mires);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void abrirFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void cerrar(View view) {
        SharedPreferences.Editor mEditor = mPreference.edit();
        mEditor.putBoolean(getString(R.string.mantenersesion), false);
        mEditor.apply();
        Toast.makeText(this, R.string.cerrar_sesion, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Main.this, Login.class));
        finish();
    }

    public void recuperar(View view) {
        SharedPreferences.Editor mEditor = mPreference.edit();
        mEditor.putBoolean(getString(R.string.mantenersesion), false);
        mEditor.apply();
        startActivity(new Intent(Main.this, Login.class));
        Toast.makeText(this, R.string.contraseña_enviada, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.fragment), bottomNavigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if (R.id.mires !=  bottomNavigationView.getSelectedItemId()) {
            bottomNavigationView.setSelectedItemId(R.id.mires);
            abrirFragment(new MisReservasFragment());
        } else {
            finish();
        }
    }

}
