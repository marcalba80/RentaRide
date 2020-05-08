package com.example.rentaride.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
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


import com.bumptech.glide.Glide;
import com.example.rentaride.BuildConfig;
import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Screens.Login;
import com.example.rentaride.Screens.Main;
import com.example.rentaride.Utils.Utils;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OfertaFragment extends Fragment {
    long f = 0;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    int color = 0, actual;
    String url = "";
    private Uri imagen;
    EditText marca, modelo, fecha, matricula, potencia, año, info, precio;
    private final Map<Reserva, String> keys = new HashMap<>();
    Spinner t, com;
    CheckBox c;
    RecyclerView lv;
    List<Reserva> list = new ArrayList<>();
    AdapterEventoReservar adapterEventoReservar;
    private static final int REQUEST_CHECK_SETTINGS = 3;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private Boolean mRequestingLocationUpdates;
    private boolean isRequestRequired;
    private String mLastUpdateTime;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private KProgressHUD k;
    private View rootView;

    public OfertaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRequestingLocationUpdates = false;
        isRequestRequired = true;
        mLastUpdateTime = "";

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        mSettingsClient = LocationServices.getSettingsClient(rootView.getContext());

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_oferta, container, false);
        inicializar(rootView);
        return rootView;
    }

    private void inicializar(View v) {
        final FitButton imagen, oferta;
        final LinearLayout extra;
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
        k = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Subiendo foto...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        extra.setVisibility(View.VISIBLE);
                        color = ContextCompat.getColor(getContext(),R.color.C1);
                        break;
                    case 1:
                        extra.setVisibility(View.VISIBLE);
                        color = ContextCompat.getColor(getContext(),R.color.C2);
                        break;
                    case 2:
                        extra.setVisibility(View.GONE);
                        color = ContextCompat.getColor(getContext(),R.color.C3);
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
                editarImagen(view);
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
                    if (año.getText().toString().equals("") || año.getText().toString().length() != 4) {
                        año.setError(getString(R.string.año));
                        return;
                    }
                    if (matricula.getText().toString().equals("") || matricula.getText().toString().length() != 7) {
                        matricula.setError(getString(R.string.matricula));
                        return;
                    }
                }
                if(url.equals(""))
                    new AlertDialog.Builder(getContext())
                            .setTitle("No hay imagen")
                            .setMessage("¿Seguro que quiere publicar el vehiculo sin imagen?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    subir();
                                }
                            })
                            .setNegativeButton(android.R.string.no,null).show();
                else{
                    subir();
                }
            }
        });
        obtener();
    }

    public void subir(){
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Vehiculo v = new Vehiculo(t.getSelectedItemPosition(), marca.getText().toString(), modelo.getText().toString(), año.getText().toString(), info.getText().toString(), matricula.getText().toString(), potencia.getText().toString() + getString(R.string.cv), com.getSelectedItem().toString(), url, c.isChecked(), Double.parseDouble(precio.getText().toString()));
        Reserva r = new Reserva(color, f, v, obtenerUbicacion(getContext()), FirebaseAuth.getInstance().getCurrentUser().getUid(), mPreference.getString(getString(R.string.preftelefono), ""));
        FirebaseFirestore.getInstance().collection("Reservas").add(r);
        list.add(r);
        Toast.makeText(getContext(), R.string.ofveh, Toast.LENGTH_SHORT).show();
        adapterEventoReservar.clear();
        adapterEventoReservar.addAll(list);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(adapterEventoReservar);
    }

    public void obtener() {
        k = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Espere...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        if(!list.isEmpty()) {
            list.clear();
            keys.clear();
            adapterEventoReservar.clear();
        }
        FirebaseFirestore.getInstance().collection("Reservas").whereEqualTo("idofertor", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                Reserva r = document.toObject(Reserva.class);
                                keys.put(r, document.getId());
                                list.add(r);
                            }
                            adapterEventoReservar = new AdapterEventoReservar(list);
                            adapterEventoReservar.setOnItemClickListener(new AdapterEventoReservar.ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    Intent i = new Intent(getContext(), DetallesReserva.class);
                                    i.putExtra(getString(R.string.otheruid), list.get(position).getIDOfertor());
                                    i.putExtra(getString(R.string.ownuid), list.get(position).getIDCliente());
                                    i.putExtra(getString(R.string.keyid),keys.get(list.get(position)));
                                    i.putExtra(getString(R.string.ve), list.get(position).getV());
                                    i.putExtra(getString(R.string.da), list.get(position).getTimeInMillis());
                                    i.putExtra(getString(R.string.telef), list.get(position).getTelefonoC());
                                    i.putExtra(getString(R.string.lat), list.get(position).getLatitud());
                                    i.putExtra(getString(R.string.lon), list.get(position).getLongitud());
                                    i.putExtra(getString(R.string.ac), 0);
                                    i.putExtra(getString(R.string.reservar), list.get(position).isReservado());
                                    startActivityForResult(i, 2);
                                }
                            });
                            lv.setHasFixedSize(true);
                            lv.setLayoutManager(new LinearLayoutManager(getContext()));
                            lv.setAdapter(adapterEventoReservar);
                            k.dismiss();
                        }
                    }
                });
    }

    public Location obtenerUbicacion(Context mContext) {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        assert lm != null;
        Location redMobil = null, gps = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    mRequestingLocationUpdates = false;
                    break;
            }
        } else if (resultCode == -1) {
            if (requestCode == 2) {
                Toast.makeText(getContext(), "Se ha eliminado la oferta correctamente!", Toast.LENGTH_SHORT).show();
                obtener();
            } else {
                if(data.getData() != null){
                    imagen = data.getData();
                    updatePhotograph();
                }
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkPermissions()) {
            requestPermissions();
        }

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(rootView.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
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
            showSnackbar(R.string.permission_rationale, android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestLocationActivity();
                }
            });

        } else {
            requestLocationActivity();
        }
    }
    private void requestLocationActivity(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                boolean showRequestRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (!showRequestRationale){
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_CHECK_SETTINGS);
                                }
                            });

                    isRequestRequired = false;
                }
            }
        }
    }

    public void editarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            boolean shouldProvideRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
            if (shouldProvideRationale) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Permiso")
                        .setMessage("Se necesitan permisos para usar esta aplicación.")
                        .setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        2);
                            }
                        })
                        .setNegativeButton("Más tarde", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "No se han podido conceder los permisos", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escoja una imagen"), 1);
    }

    private void updatePhotograph() {
        k.show();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Images/cars/" + FirebaseAuth.getInstance().getUid())
                .child(Objects.requireNonNull(imagen.getLastPathSegment()));

        ref.putFile(imagen).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    url = task.getResult().toString();
                    k.dismiss();
                }
            }
        });
    }

}

