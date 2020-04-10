package com.example.rentaride.Screens;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentaride.Fragments.OfertaFragment;
import com.example.rentaride.Fragments.ReservaFragment;
import com.example.rentaride.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Main extends AppCompatActivity {
    private Button reserva, oferta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.configscreen, false);

        reserva = findViewById(R.id.reserva_frag);
        oferta = findViewById(R.id.oferta_frag);

        reserva(reserva);
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

    public void reserva(View view){
        view.setSelected(true);
        oferta.setSelected(false);
        getFragmentManager().beginTransaction().replace(R.id.mainFrag, new ReservaFragment()).commit();
    }

    public void oferta(View view){
        view.setSelected(true);
        reserva.setSelected(false);
        getFragmentManager().beginTransaction().replace(R.id.mainFrag, new OfertaFragment()).commit();
    }
}
