package com.example.rentaride.Fragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.github.nikartm.button.FitButton;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OfertaFragment extends Fragment {
    long f = 0;
    int color = 0;
    RecyclerView lv;
    List<Reserva> list = new ArrayList<>();
    AdapterEventoReservar adapterEventoReservar;

    public OfertaFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
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
                switch (i){
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
                fecha.setText(dayOfMonth +"/"+(monthOfYear+1)+"/"+year);
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
                if(marca.getText().toString().equals("")){
                    marca.setError("Introduzca una marca");
                    return;
                }
                if(modelo.getText().toString().equals("")){
                    modelo.setError("Introduzca un modelo");
                    return;
                }
                if(fecha.getText().toString().equals("")){
                    fecha.setError("Introduzca una fecha");
                    return;
                }
                if(precio.getText().toString().equals("")){
                    precio.setError("Introduzca un precio");
                    return;
                }
                if(t.getSelectedItemPosition() != 2){
                    if(potencia.getText().toString().equals("")){
                        potencia.setError("Introduzca una potencia");
                        return;
                    }
                    if(año.getText().toString().equals("")){
                        año.setError("Introduzca un año");
                        return;
                    }
                    if(matricula.getText().toString().equals("")){
                        matricula.setError("Introduzca una matricula");
                        return;
                    }
                }
                Vehiculo v = new Vehiculo(t.getSelectedItemPosition(), marca.getText().toString(),modelo.getText().toString(),año.getText().toString(), info.getText().toString(), "636666663", matricula.getText().toString(),potencia.getText().toString()+" cv", com.getSelectedItem().toString(), "",c.isChecked());
                Reserva r = new Reserva(color, f,  v, Double.parseDouble(precio.getText().toString()));
                list.add(r);
                adapterEventoReservar = new AdapterEventoReservar(list);
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(adapterEventoReservar);
            }
        });
        obtener();
        adapterEventoReservar = new AdapterEventoReservar(list);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(adapterEventoReservar);
    }

    public void obtener(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == -1 && requestCode == 1){
            Toast.makeText(getContext(), "Se ha guardado la imagen correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}
