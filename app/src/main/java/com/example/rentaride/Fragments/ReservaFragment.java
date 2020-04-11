package com.example.rentaride.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Spinner;


import androidx.fragment.app.Fragment;

import com.example.rentaride.R;

public class ReservaFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    Spinner spin;
    String[] typeFuel = {getString(R.string.gasolina), getString(R.string.diesel), getString(R.string.hibrido), getString(R.string.electrico)};


    public ReservaFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spin = (Spinner) rootView.findViewById(R.id.spin);
        spin.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reserva, container, false);
        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void buscar(View view){

    }

    public void mapa(View view){

    }
}
