package com.example.rentaride.Fragments;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
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


import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.rentaride.Utils.Utils.motoprueba;

public class OfertaFragment extends Fragment {
    View rootView;
    long f = 0;
    int color = 0, actual;
    RecyclerView lv;
    List<Reserva> list = new ArrayList<>();
    AdapterEventoReservar adapterEventoReservar;

    // Location

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;
    private boolean isRequestRequired;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

//***********************************************************************************************

    public OfertaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Location
        mRequestingLocationUpdates = false;
        isRequestRequired = true;
        mLastUpdateTime = "";

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        mSettingsClient = LocationServices.getSettingsClient(rootView.getContext());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_oferta, container, false);
        inicializar(rootView);
        return rootView;
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
                f = myCalendar.getTimeInMillis();
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
                    marca.setError("Introduzca una marca");
                    return;
                }
                if (modelo.getText().toString().equals("")) {
                    modelo.setError("Introduzca un modelo");
                    return;
                }
                if (fecha.getText().toString().equals("")) {
                    fecha.setError("Introduzca una fecha");
                    return;
                }
                if (precio.getText().toString().equals("")) {
                    precio.setError("Introduzca un precio");
                    return;
                }
                if (t.getSelectedItemPosition() != 2) {
                    if (potencia.getText().toString().equals("")) {
                        potencia.setError("Introduzca una potencia");
                        return;
                    }
                    if (año.getText().toString().equals("")) {
                        año.setError("Introduzca un año");
                        return;
                    }
                    if (matricula.getText().toString().equals("")) {
                        matricula.setError("Introduzca una matricula");
                        return;
                    }
                }
                double[] coord = locate();
                Vehiculo v = new Vehiculo(t.getSelectedItemPosition(),
                        marca.getText().toString(),
                        modelo.getText().toString(),
                        año.getText().toString(),
                        info.getText().toString(),
                        "636666663",
                        matricula.getText().toString(),
                        potencia.getText().toString() + " cv",
                        com.getSelectedItem().toString(),
                        fecha.getText().toString(),
                        Double.parseDouble(precio.getText().toString()),
                        "",
                        c.isChecked(),
                        //41.621625, 0.638906);
                        coord[0], coord[1]);
                Reserva r = new Reserva(color, f, v);
                list.add(r);
                adapterEventoReservar = new AdapterEventoReservar(list);
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(adapterEventoReservar);
                adapterEventoReservar.setOnItemClickListener(new AdapterEventoReservar.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent i = new Intent(getContext(), DetallesReserva.class);
                        actual = list.indexOf(list.get(position));
                        i.putExtra("ve", list.get(position).getV());
                        i.putExtra("ac", 0);
                        startActivityForResult(i, 2);
                    }
                });
            }
        });
        obtener();
        adapterEventoReservar = new AdapterEventoReservar(list);
        adapterEventoReservar.setOnItemClickListener(new AdapterEventoReservar.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(getContext(), DetallesReserva.class);
                actual = list.indexOf(list.get(position));
                i.putExtra("ve", list.get(position).getV());
                i.putExtra("ac", 0);
                startActivityForResult(i, 2);
            }
        });
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(adapterEventoReservar);
    }

    public void obtener() {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = s.parse(motoprueba.getFecha());
            list.add(new Reserva(getResources().getColor(R.color.C2, null), date.getTime(), motoprueba));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    break;
            }
        } else if (resultCode == -1) {
            if (requestCode == 2) {
                list.remove(actual);
                adapterEventoReservar.clear();
                Toast.makeText(getContext(), "Se ha eliminado la oferta correctamente!", Toast.LENGTH_SHORT).show();
                adapterEventoReservar = new AdapterEventoReservar(list);
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(adapterEventoReservar);
            } else {
                Toast.makeText(getContext(), "Se ha guardado la imagen correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Location

    private double[] locate() {
        double[] coord = new double[2];
        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        /*createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();*/

        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());

        LocationManager location = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions();
        }
        Location loc = location.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        /*coord[0] = mCurrentLocation.getLatitude();
        coord[1] = mCurrentLocation.getLongitude();*/
        coord[0] = loc.getLatitude();
        coord[1] = loc.getLongitude();
        return coord;
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Creates a callback for receiving location events.
     */
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
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (!checkPermissions()) {
            requestPermissions();
        }

    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(rootView.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                rootView.findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestLocationActivity();
                }
            });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            /*ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);*/
            requestLocationActivity();
        }
    }

    /**
     * Activity call
     */
    private void requestLocationActivity(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                boolean showRequestRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (!showRequestRationale){
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
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
}
