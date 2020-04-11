package com.example.rentaride.Fragments;

import androidx.fragment.app.Fragment;

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


import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.github.nikartm.button.FitButton;

import java.util.Calendar;
import java.util.Date;

public class OfertaFragment extends Fragment {
    long f = 0;
    int color = 0;

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
        inizializar(v);
        return v;
    }

    private void inizializar(View v) {
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
                if(marca.getText().toString()==""){
                    marca.setError("Introduzca una marca");
                    return;
                }
                if(modelo.getText().toString()==""){
                    modelo.setError("Introduzca un modelo");
                    return;
                }
                if(fecha.getText().toString()==""){
                    fecha.setError("Introduzca una fecha");
                    return;
                }
                if(precio.getText().toString()==""){
                    precio.setError("Introduzca un precio");
                    return;
                }
                if(t.getSelectedItemPosition() != 2){
                    if(potencia.getText().toString()==""){
                        potencia.setError("Introduzca una potencia");
                        return;
                    }
                    if(año.getText().toString()==""){
                        año.setError("Introduzca un año");
                        return;
                    }
                    if(matricula.getText().toString()==""){
                        matricula.setError("Introduzca una matricula");
                        return;
                    }
                }
                Vehiculo v = new Vehiculo(t.getSelectedItemPosition(), marca.getText().toString(),modelo.getText().toString(),año.getText().toString(), info.getText().toString(), "636666663", matricula.getText().toString(),potencia.getText().toString()+" cv", com.getSelectedItem().toString(), "",c.isChecked());
                Reserva r = new Reserva(color, f,  v, Double.parseDouble(precio.getText().toString()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == -1 && requestCode == 1){
            Toast.makeText(getContext(), "Se ha guardado la imagen correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}
