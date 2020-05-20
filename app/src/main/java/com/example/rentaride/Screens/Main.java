package com.example.rentaride.Screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentaride.Fragments.MisReservasFragment;
import com.example.rentaride.Fragments.OfertaFragment;
import com.example.rentaride.Fragments.PerfilFragment;
import com.example.rentaride.Fragments.ReservaFragment;
import com.example.rentaride.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Objects;

public class Main extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    SharedPreferences mPreference;
    Mapa.RedReceiver rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rr = new Mapa.RedReceiver();
        registerReceiver(rr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mPreference = PreferenceManager.getDefaultSharedPreferences(Main.this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("tok", instanceIdResult.getToken());
                FirebaseFirestore
                        .getInstance()
                        .collection("Tokens")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(hm);
                createNotificationChannel();

            }
        });
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
        new AlertDialog.Builder(Objects.requireNonNull(this))
                .setTitle("Cambiar Contraseña")
                .setMessage("¿Seguro que desea cambiar la contraseña?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
                        Toast.makeText(getApplicationContext(), "Se ha enviado el correo de recuperación! Revise su buzón.", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Main.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rr);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notificacionesn);
            String description = getString(R.string.notificacionesd);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notificacionesn), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
