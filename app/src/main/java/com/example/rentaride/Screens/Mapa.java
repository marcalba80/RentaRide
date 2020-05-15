package com.example.rentaride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.ContentLengthInputStream;
import com.example.rentaride.Fragments.ReservaFragment;
import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;
import com.example.rentaride.Utils.Utils;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.View.GONE;
import static com.google.android.gms.maps.CameraUpdateFactory.zoomIn;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    GoogleMap googleMap;
    double lat, lon;
    String tit;
    CheckBox checkCotxe, checkMoto, checkbici;
    private static boolean mobil = false, detalle = true, mostrar = true;
    List<Reserva> coches = new ArrayList<>();
    private final Map<Reserva, String> keys = new HashMap<>();
    private final Map<Marker, String> keysM = new HashMap<>();
    List<Reserva> motos = new ArrayList<>();
    List<Reserva> bicis = new ArrayList<>();
    List <Marker> cotxeMarkers = new ArrayList<>();
    List <Marker> motoMarkers = new ArrayList<>();
    List <Marker> biciMarkers = new ArrayList<>();
    Map<Marker, Reserva> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        TextView error = findViewById(R.id.mensajeerror);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sPref = sharedPrefs.getString("PreferenciaRed", "Todas");
        if((sPref.equals("Wi-Fi") && mobil) || !mostrar)
            error.setVisibility(View.VISIBLE);
        else
            if(getIntent().hasExtra(getString(R.string.lat))) {
                LinearLayout l = findViewById(R.id.checkboxes);
                l.setVisibility(GONE);
                FrameLayout f = findViewById(R.id.map);
                f.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
                lat = getIntent().getDoubleExtra(getString(R.string.lat), 0.0);
                lon = getIntent().getDoubleExtra(getString(R.string.lon), 0.0);
                tit = getIntent().getStringExtra(getString(R.string.tit));
            }else{
                detalle = false;
                obtenerReservas();
                checkCotxe = findViewById(R.id.cotxes);
                checkCotxe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean bol) {
                        if (bol) {
                            showCotxe();
                        } else {
                            hideCotxes();
                        }
                    }
                });

                checkMoto = findViewById(R.id.motos);
                checkMoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean bol) {
                        if (bol) {
                            showMotos();
                        } else {
                            hideMotos();
                        }
                    }
                });
                checkbici = findViewById(R.id.bicicleta);
                checkbici.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean bol) {
                        if (bol) {
                            showBici();
                        } else {
                            hideBici();
                        }
                    }
                });
            }
    }

    public void obtenerReservas() {
        final KProgressHUD k = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Espere...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        if(!coches.isEmpty() || !motos.isEmpty() || !bicis.isEmpty()) {
            coches.clear();
            cotxeMarkers.clear();
            motos.clear();
            motoMarkers.clear();
            bicis.clear();
            biciMarkers.clear();
            keys.clear();
        }
        FirebaseFirestore.getInstance().collection("Reservas").whereEqualTo("reservado", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            List<Reserva> list = new ArrayList<>();
                            for (DocumentSnapshot document : l) {
                                Reserva r = document.toObject(Reserva.class);
                                if(!r.getIDOfertor().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    keys.put(r, document.getId());
                                    list.add(r);
                                }
                            }
                            for (Reserva res : list) {
                                switch(res.getV().getType()){
                                    case 0:
                                        coches.add(res);
                                        break;
                                    case 1:
                                        motos.add(res);
                                        break;
                                    case 2:
                                        bicis.add(res);
                                        break;
                                }
                            }
                            k.dismiss();
                        }
                    }
                });
    }

    public void showCotxe() {
        cotxeMarkers.clear();
        for (Reserva loc : coches) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLatitud(),loc.getLongitud()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitud(),loc.getLongitud())).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            cotxeMarkers.add(marker);
            map.put(marker, loc);
            keysM.put(marker,keys.get(loc));
        }
    }

    public void showMotos() {
        motoMarkers.clear();
        for (Reserva loc : motos) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLatitud(),loc.getLongitud()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitud(),loc.getLongitud())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            motoMarkers.add(marker);
            map.put(marker, loc);
            keysM.put(marker,keys.get(loc));
        }
    }

    public void showBici() {
        biciMarkers.clear();
        for (Reserva loc : bicis) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLatitud(),loc.getLongitud()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitud(),loc.getLongitud())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            biciMarkers.add(marker);
            map.put(marker, loc);
            keysM.put(marker,keys.get(loc));
        }
    }

    public void hideCotxes() {
        for (Marker marker : cotxeMarkers ) {
            marker.remove();
            map.remove(marker);
        }
    }

    public void hideMotos() {
        for (Marker marker : motoMarkers) {
            marker.remove();
            map.remove(marker);
        }
    }

    public void hideBici() {
        for (Marker marker : biciMarkers) {
            marker.remove();
            map.remove(marker);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Reserva r = map.get(marker);
        Intent i = new Intent(Mapa.this, DetallesReserva.class);
        i.putExtra(getString(R.string.otheruid), r.getIDOfertor());
        i.putExtra(getString(R.string.ownuid), r.getIDCliente());
        i.putExtra(getString(R.string.keyid), keysM.get(marker));
        i.putExtra(getString(R.string.ve), r.getV());
        i.putExtra(getString(R.string.da), r.getTimeInMillis());
        i.putExtra(getString(R.string.telef),r.getTelefonoO());
        i.putExtra(getString(R.string.lat), r.getLatitud());
        i.putExtra(getString(R.string.lon), r.getLongitud());
        i.putExtra(getString(R.string.map), true);
        i.putExtra(getString(R.string.ac), 2);
        i.putExtra(getString(R.string.reservar), r.isReservado());
        startActivityForResult(i, 2);
        return false;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(Mapa.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            new AlertDialog.Builder(Mapa.this)
                    .setTitle(R.string.needed)
                    .setMessage(R.string.prop)
                    .setPositiveButton(R.string.su, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationActivity();
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showSnackbar(R.string.perm,
                                    android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            requestLocationActivity();
                                        }
                                    });
                        }
                    }).create().show();
        } else {
            requestLocationActivity();
        }
    }

    private void requestLocationActivity(){
        ActivityCompat.requestPermissions(Mapa.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            zoomIn();
        }
    }

    public void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }else{
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if(detalle){
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat,lon))
                        .title(tit)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lon)).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }else{
                googleMap.setOnMarkerClickListener(this);
            }
            zoomIn();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    public static class RedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                mostrar = true;
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    mobil = true;
                } else {
                    mobil = false;
                }
            } else {
                mostrar = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == -1) {
                hideCotxes();
                hideMotos();
                hideBici();
                checkCotxe.setChecked(false);
                checkMoto.setChecked(false);
                checkbici.setChecked(false);
                obtenerReservas();
                Toast.makeText(this, getString(R.string.reservacor), Toast.LENGTH_SHORT).show();

            }
        }
    }
}
