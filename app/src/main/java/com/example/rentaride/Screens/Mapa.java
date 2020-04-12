package com.example.rentaride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.google.android.gms.maps.CameraUpdateFactory.zoomIn;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    GoogleMap googleMap;
    double lat, lon;
    String tit;
    List<Reserva> coches = new ArrayList<>();
    List<Reserva> motos = new ArrayList<>();
    List<Reserva> bicis = new ArrayList<>();
    List <Marker> cotxeMarkers = new ArrayList<>();
    List <Marker> motoMarkers = new ArrayList<>();
    List <Marker> biciMarkers = new ArrayList<>();
    Map<Marker, Reserva> map = new HashMap<>();


    boolean detalle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
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
            CheckBox checkCotxe = findViewById(R.id.cotxes);
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

            CheckBox checkMoto = findViewById(R.id.motos);
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
            CheckBox checkbici = findViewById(R.id.bicicleta);
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

    private void obtenerReservas() {
        List<Reserva> l = new ArrayList<>();
        for(Reserva r : Utils.obtenerReservas()){
            if(!r.isReservado() && !r.getIDOfertor().equals(Utils.ID)){
                l.add(r);
            }
        }
        for (Reserva res : l) {
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
    }

    public void showCotxe() {
        cotxeMarkers.clear();
        for (Reserva loc : coches) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude())).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            cotxeMarkers.add(marker);
            map.put(marker, loc);
        }
    }

    public void showMotos() {
        motoMarkers.clear();
        for (Reserva loc : motos) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            motoMarkers.add(marker);
            map.put(marker, loc);
        }
    }

    public void showBici() {
        biciMarkers.clear();
        for (Reserva loc : bicis) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude()))
                    .title(loc.getV().getMarca() + " " + loc.getV().getModelo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLocation().getLatitude(),loc.getLocation().getLongitude())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            biciMarkers.add(marker);
            map.put(marker, loc);
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
        i.putExtra(getString(R.string.ve), r.getV());
        i.putExtra(getString(R.string.da), r.getTimeInMillis());
        i.putExtra(getString(R.string.telef),r.getTelefonoO());
        i.putExtra(getString(R.string.lat), r.getLocation().getLatitude());
        i.putExtra(getString(R.string.lon), r.getLocation().getLongitude());
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
}
