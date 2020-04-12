package com.example.rentaride.Fragments;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.rentaride.BuildConfig;
import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Utils.Utils;
import com.github.nikartm.button.FitButton;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.rentaride.Utils.Utils.motoprueba;

public class OfertaFragment extends Fragment {
    long f = 0;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    int color = 0, actual;
    RecyclerView lv;
    List<Reserva> list = new ArrayList<>();
    AdapterEventoReservar adapterEventoReservar;

    public OfertaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_oferta, container, false);
        inicializar(v);
        return v;
    }

    private void inicializar(View v) {
        FitButton imagen, oferta;
        final LinearLayout extra;
        final Spinner t, com;
        final EditText marca, modelo, fecha, matricula, potencia, año, info, precio;
        final CheckBox c;
        extra = v.findViewById(R.id.extra);
        t = v.findViewById(R.id.tip);
        marca = v.findViewById(R.id.reservaMarca);
        modelo = v.findViewById(R.id.reservaModelo);
        fecha = v.findViewById(R.id.fecha);
        c = v.findViewById(R.id.adaptado);
        com = v.findViewById(R.id.spin);
        lv = v.findViewById(R.id.list);
        imagen = v.findViewById(R.id.imagen);
        oferta = v.findViewById(R.id.ofertar);
        matricula = v.findViewById(R.id.matricula);
        año = v.findViewById(R.id.año);
        potencia = v.findViewById(R.id.potencia);
        info = v.findViewById(R.id.info);
        precio = v.findViewById(R.id.precio);

        t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        extra.setVisibility(View.VISIBLE);
                        color = getResources().getColor(R.color.C1);
                        break;
                    case 1:
                        extra.setVisibility(View.VISIBLE);
                        color = getResources().getColor(R.color.C2);
                        break;
                    case 2:
                        extra.setVisibility(View.GONE);
                        color = getResources().getColor(R.color.C3);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    f = s.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year+ " 00:00").getTime();
                } catch (ParseException e) {
                    f = myCalendar.getTimeInMillis();
                }
            }

        };
        fecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marca.getText().toString().equals("")) {
                    marca.setError(getString(R.string.marca));
                    return;
                }
                if (modelo.getText().toString().equals("")) {
                    modelo.setError(getString(R.string.modelo));
                    return;
                }
                if (fecha.getText().toString().equals("")) {
                    fecha.setError(getString(R.string.fecha));
                    return;
                }
                if (precio.getText().toString().equals("")) {
                    precio.setError(getString(R.string.precio));
                    return;
                }
                if (t.getSelectedItemPosition() != 2) {
                    if (potencia.getText().toString().equals("")) {
                        potencia.setError(getString(R.string.potencia));
                        return;
                    }
                    if (año.getText().toString().equals("")) {
                        año.setError(getString(R.string.año));
                        return;
                    }
                    if (matricula.getText().toString().equals("")) {
                        matricula.setError(getString(R.string.matricula));
                        return;
                    }
                }
                Vehiculo v = new Vehiculo(t.getSelectedItemPosition(), marca.getText().toString(), modelo.getText().toString(), año.getText().toString(), info.getText().toString(), "636666663", matricula.getText().toString(), potencia.getText().toString() + getString(R.string.cv), com.getSelectedItem().toString(), "", c.isChecked());
                Reserva r = new Reserva(color, f, v, Double.parseDouble(precio.getText().toString()), obtenerUbicacion(getContext()));
                list.add(r);
                adapterEventoReservar = new AdapterEventoReservar(list);
                adapterEventoReservar.setOnItemClickListener(new AdapterEventoReservar.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent i = new Intent(getContext(), DetallesReserva.class);
                        actual = list.indexOf(list.get(position));
                        i.putExtra(getString(R.string.ve), list.get(position).getV());
                        i.putExtra(getString(R.string.da), list.get(position).getTimeInMillis());
                        i.putExtra(getString(R.string.pr), list.get(position).getPrecio());
                        i.putExtra(getString(R.string.lat), list.get(position).getLocation().getLatitude());
                        i.putExtra(getString(R.string.lon), list.get(position).getLocation().getLongitude());
                        i.putExtra(getString(R.string.ac), 0);
                        startActivityForResult(i, 2);
                    }
                });
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(adapterEventoReservar);
            }
        });
        obtener();
        adapterEventoReservar = new AdapterEventoReservar(list);
        adapterEventoReservar.setOnItemClickListener(new AdapterEventoReservar.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(getContext(), DetallesReserva.class);
                actual = list.indexOf(list.get(position));
                i.putExtra(getString(R.string.ve), list.get(position).getV());
                i.putExtra(getString(R.string.da), list.get(position).getTimeInMillis());
                i.putExtra(getString(R.string.pr), list.get(position).getPrecio());
                i.putExtra(getString(R.string.lat), list.get(position).getLocation().getLatitude());
                i.putExtra(getString(R.string.lon), list.get(position).getLocation().getLongitude());
                i.putExtra(getString(R.string.ac), 0);
                startActivityForResult(i, 2);
            }
        });
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(adapterEventoReservar);
    }

    public void obtener() {
        list.add( (Reserva) Utils.obtenerReservas(getResources().getColor(R.color.C1), getResources().getColor(R.color.C2), getResources().getColor(R.color.C3)).get(0));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 2) {
                list.remove(actual);
                adapterEventoReservar.clear();
                Toast.makeText(getContext(), R.string.ofveh, Toast.LENGTH_SHORT).show();
                adapterEventoReservar = new AdapterEventoReservar(list);
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(adapterEventoReservar);
            } else {
                Toast.makeText(getContext(), R.string.guardar_correctamente, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Location obtenerUbicacion(Context mContext) {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        assert lm != null;

        Location redMobil = null, gps = null, finalLoc = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
        gps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            redMobil = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gps != null && redMobil != null) {
            if (gps.getAccuracy() > redMobil.getAccuracy())
                return redMobil;
            else
                return gps;

        } else {
            if (gps != null) {
                return gps;
            } else if (redMobil != null) {
                return redMobil;
            }
        }
        return null;
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestLocationPermissions();
                        }
                    });
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

}
