package com.example.rentaride.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEventoReservar;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.rentaride.Utils.Utils.biciprueba;
import static com.example.rentaride.Utils.Utils.cocheprueba;
import static com.example.rentaride.Utils.Utils.motoprueba;

public class ReservaFragment extends Fragment {
    View rootView;
    Spinner spin;
    String[] typeFuel = {"Gasolina", "Dieses", "Hibrido", "Electrico"};
    List<Reserva> list = new ArrayList<>();
    AdapterEventoReservar arrayAdapter;
    RecyclerView lv;


    public ReservaFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //spin = (Spinner) rootView.findViewById(R.id.spin);
       //spin.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reserva, container, false);
        inicializar(v);
        return v;
    }

    private void inicializar(View v) {
        final EditText marca, modelo;
        Button b;
        lv = v.findViewById(R.id.list);
        marca = v.findViewById(R.id.reservaMarca);
        modelo = v.findViewById(R.id.reservaModelo);
        b = v.findViewById(R.id.buscar);
        recuperar();
        arrayAdapter = new AdapterEventoReservar(list);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(arrayAdapter);
        marca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.filtrarMarca(s.toString());
            }
        });
        modelo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.filtrarModelo(s.toString());

            }
        });
    }

    private void recuperar() {
        long day = 86400000;
        Location l = new Location("");
        l.setLatitude(0.0);
        l.setLongitude(0.0);
        list.add(new Reserva(getResources().getColor(R.color.C1), new Date().getTime(),cocheprueba, 90.5,l));
        list.add(new Reserva(getResources().getColor(R.color.C2), new Date().getTime(), motoprueba, 20,l));
        list.add(new Reserva(getResources().getColor(R.color.C3), new Date().getTime(),biciprueba, 10.99,l));
        list.add(new Reserva(getResources().getColor(R.color.C1), new Date().getTime()+day,cocheprueba, 102.5,l));
        list.add(new Reserva(getResources().getColor(R.color.C2), new Date().getTime()+day*2,motoprueba, 19.95,l));
        list.add(new Reserva(getResources().getColor(R.color.C3), new Date().getTime()+day*3,biciprueba, 9.5,l));
    }
}
