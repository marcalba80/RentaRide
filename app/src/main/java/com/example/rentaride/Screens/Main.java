package com.example.rentaride.Screens;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentaride.Fragments.MisReservasFragment;
import com.example.rentaride.Fragments.OfertaFragment;
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

public class Main extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.configscreen, false);

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
                           /* case R.id.perfil:
                                abrirFragment(new OfertaFragment());
                                return true;*/
                }
                return false;
            }
        });

        bottomNavigation.setSelectedItemId(R.id.mires);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent in;
        switch (item.getItemId()) {
            case R.id.action_settings:
                in = new Intent(this, Configuracio.class);
                startActivity(in);
                return true;
            case R.id.logout:
                in = new Intent(this, Login.class);
                startActivity(in);
                return true;
            case R.id.action_calendar:
                startActivity(new Intent(this, Calendar.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void abrirFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
