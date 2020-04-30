package com.example.rentaride.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Screens.Mapa;
import com.example.rentaride.Utils.Utils;
import com.github.nikartm.button.FitButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservaFragment extends Fragment implements AdapterEventoReservar.ReservaListener {
    private int actual;
    private List<Reserva> list = new ArrayList<>();
    private AdapterEventoReservar arrayAdapter;
    private RecyclerView lv;
    private long f;


    public ReservaFragment(){

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
        View v = inflater.inflate(R.layout.fragment_reserva, container, false);
        inicializar(v);
        return v;
    }

    private void inicializar(View v) {
        final EditText marca, modelo,fecha;
        final Spinner t, com;
        final CheckBox ch;
        lv = v.findViewById(R.id.list);
        t = v.findViewById(R.id.tip);
        final LinearLayout extra = v.findViewById(R.id.extra);
        com = v.findViewById(R.id.spin);
        marca = v.findViewById(R.id.reservaMarca);
        modelo = v.findViewById(R.id.reservaModelo);
        fecha = v.findViewById(R.id.fecha);
        ch = v.findViewById(R.id.adaptado);
        recuperar();
        arrayAdapter = new AdapterEventoReservar(list);
        arrayAdapter.setOnFilteredItemClickListener(this);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(arrayAdapter);
        FitButton button = v.findViewById(R.id.mapa);
        FitButton buscar = v.findViewById(R.id.buscar);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fecha.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
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

        t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2) {
                    extra.setVisibility(View.GONE);
                } else {
                    extra.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Mapa.class));
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.clear();
                arrayAdapter.addAll(list);
                arrayAdapter.notifyDataSetChanged();
                if (!marca.getText().toString().equals("")) {
                    arrayAdapter.filtrarMarca(marca.getText().toString());
                }
                if (!modelo.getText().toString().equals("")) {
                    arrayAdapter.filtrarModelo(modelo.getText().toString());

                }
                if (!fecha.getText().toString().equals("")) {
                    arrayAdapter.filtrarFecha(f);
                }
                if(com != null && com.getSelectedItem() !=null ) arrayAdapter.filtrarCombustible(com.getSelectedItem().toString());
                if (ch.isChecked()){
                    arrayAdapter.filtrarAdaptado(true);
                }else{
                    arrayAdapter.filtrarAdaptado(false);
                }
                if(t.getSelectedItem() !=null ) arrayAdapter.filtrarTipo(t.getSelectedItemPosition());
            }
        });

    }

    private void recuperar() {
        for(Reserva r : Utils.obtenerReservas()){
            if(!r.isReservado() && !r.getIDOfertor().equals(Utils.ID)){
                list.add(r);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == -1) {
                list.remove(actual);
                arrayAdapter.clear();
                arrayAdapter.addAll(list);
                Toast.makeText(getActivity(), getString(R.string.reservacor), Toast.LENGTH_SHORT).show();
                lv.setHasFixedSize(true);
                lv.setLayoutManager(new LinearLayoutManager(getContext()));
                lv.setAdapter(arrayAdapter);
            }
        }
    }

    @Override
    public void onReservaSelected(Reserva r) {
        Intent i = new Intent(getContext(), DetallesReserva.class);
        actual = list.indexOf(r);
        i.putExtra(getString(R.string.ve), r.getV());
        i.putExtra(getString(R.string.da), r.getTimeInMillis());
        i.putExtra(getString(R.string.telef),r.getTelefonoC());
        i.putExtra(getString(R.string.lat), r.getLatitud());
        i.putExtra(getString(R.string.lon), r.getLongitud());
        i.putExtra(getString(R.string.ac), 2);
        i.putExtra(getString(R.string.reservar), r.isReservado());
        startActivityForResult(i, 2);
    }
}
