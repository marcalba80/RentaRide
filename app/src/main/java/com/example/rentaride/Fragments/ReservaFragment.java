package com.example.rentaride.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Utils.Utils;
import com.github.nikartm.button.FitButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservaFragment extends Fragment{
    View rootView;

    EditText marca, modelo, fecha;
    Spinner spinType, spinComb;
    CheckBox adaptado;


    ListView listReserva;


    public ReservaFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinType = rootView.findViewById(R.id.spintype);
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout extra = rootView.findViewById(R.id.extra);
                switch (position){
                    case 0:
                    case 1:
                        extra.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        extra.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinComb = rootView.findViewById(R.id.spincombustible);
        spinComb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        marca = rootView.findViewById(R.id.reservaMarca);
        modelo = rootView.findViewById(R.id.reservaModelo);
        fecha = rootView.findViewById(R.id.date);
        adaptado = rootView.findViewById(R.id.adaptado);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String day = String.valueOf(dayOfMonth);
                String month = String.valueOf(monthOfYear+1);
                if(dayOfMonth < 10) day = "0" + day;
                if(monthOfYear < 10) month = "0" + month;
                fecha.setText(day +"/"+month+"/"+year);
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

        FitButton buscar = rootView.findViewById(R.id.buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        FitButton mapa = rootView.findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapa();
            }
        });

        listReserva = rootView.findViewById(R.id.reservalist);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reserva, container, false);
        return rootView;
    }

    public void buscar(){
        final List<Vehiculo> listvehiculo = new ArrayList<>();
        listvehiculo.add(Utils.cocheprueba);
        listvehiculo.add(Utils.motoprueba);
        listvehiculo.add(Utils.biciprueba);

        if(spinType.getSelectedItemPosition() < 3) {
            checkEmptyViews();
            final List<Vehiculo> filtrado = filtrar(listvehiculo);
            listReserva.setAdapter(new CustomArrayAdapter(rootView.getContext(), filtrado));
            listReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(rootView.getContext(), DetallesReserva.class);
                    i.putExtra("ve", filtrado.get(position));
                    i.putExtra("da", filtrado.get(position).getFecha());
                    i.putExtra("pr", filtrado.get(position).getPrecio());
                    i.putExtra("ac", 2);
                    startActivityForResult(i, 2);
                }
            });
        }else {
            listReserva.setAdapter(new CustomArrayAdapter(rootView.getContext(), listvehiculo));
            listReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(rootView.getContext(), DetallesReserva.class);
                    i.putExtra("ve", listvehiculo.get(position));
                    i.putExtra("da", listvehiculo.get(position).getFecha());
                    i.putExtra("pr", listvehiculo.get(position).getPrecio());
                    i.putExtra("ac", 2);
                    startActivityForResult(i, 2);
                }
            });
        }
    }

    private void checkEmptyViews() {
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
    }

    private List<Vehiculo> filtrar(List<Vehiculo> listvehiculo) {
        List<Vehiculo> filtrados = new ArrayList<>();
        for(Vehiculo v: listvehiculo) {
            if (spinType.getSelectedItemPosition() == 2) {
                if (v.getType() == spinType.getSelectedItemPosition()
                        && v.getMarca().equals(marca.getText().toString())
                        && v.getModelo().equals(modelo.getText().toString())
                        && v.getFecha().equals(fecha.getText().toString())) {
                    filtrados.add(v);
                }
            }else if(spinType.getSelectedItemPosition() == 1){
                if (v.getType() == spinType.getSelectedItemPosition()
                        && v.getMarca().equals(marca.getText().toString())
                        && v.getModelo().equals(modelo.getText().toString())
                        && v.getFecha().equals(fecha.getText().toString())
                        && v.getCombustible().equals(spinComb.getSelectedItem().toString())) {
                    filtrados.add(v);
                }
            }else{
                if (v.getType() == spinType.getSelectedItemPosition()
                        && v.getMarca().equals(marca.getText().toString())
                        && v.getModelo().equals(modelo.getText().toString())
                        && v.getFecha().equals(fecha.getText().toString())
                        && v.getCombustible().equals(spinComb.getSelectedItem().toString())
                        && v.isAdaptado() == adaptado.isChecked()) {
                    filtrados.add(v);
                }
            }
        }
        return filtrados;
    }

    public void mapa(){

    }

    private class CustomArrayAdapter extends ArrayAdapter<Vehiculo>{
        private Context context;
        private List<Vehiculo> vehiculoList;

        public CustomArrayAdapter(@NonNull Context context, List<Vehiculo> list){
            super(context, 0, list);
            this.context = context;
            this.vehiculoList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null) listItem = LayoutInflater.from(context).inflate(R.layout.item_event_reservar, parent, false);
            Vehiculo currentVehiculo = vehiculoList.get(position);

            TextView tipo = listItem.findViewById(R.id.tipo);
            if(currentVehiculo.getType() == 0){
                tipo.setText("Coche");
                //convertView.getBackground().setTint(getResources().getColor(R.color.C1, null));
                listItem.findViewById(R.id.card).getBackground().setTint(getResources().getColor(R.color.C1, null));
            }else if(currentVehiculo.getType() == 1){
                tipo.setText("Moto");
                //convertView.getBackground().setTint(getResources().getColor(R.color.C2, null));
                listItem.findViewById(R.id.card).getBackground().setTint(getResources().getColor(R.color.C2, null));
            }else{
                tipo.setText("Bicicleta");
                //convertView.getBackground().setTint(getResources().getColor(R.color.C3, null));
                listItem.findViewById(R.id.card).getBackground().setTint(getResources().getColor(R.color.C3, null));
            }

            TextView fecha = listItem.findViewById(R.id.hora);
            fecha.setText(currentVehiculo.getFecha());

            TextView desc = listItem.findViewById(R.id.desc);
            desc.setText(currentVehiculo.getMarca() + " " + currentVehiculo.getModelo());

            TextView precio = listItem.findViewById(R.id.precio);
            precio.setText(Double.toString(currentVehiculo.getPrecio()) + "€");

            return listItem;
        }
    }
}
