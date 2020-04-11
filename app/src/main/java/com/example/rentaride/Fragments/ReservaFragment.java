package com.example.rentaride.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.example.rentaride.Utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservaFragment extends Fragment{
    View rootView;

    EditText marca, modelo, fecha;
    Spinner spin;
    CheckBox adaptado;


    ListView listReserva;

    ImageView listImage;
    TextView listText;


    public ReservaFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spin = (Spinner) rootView.findViewById(R.id.spin);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buscar = rootView.findViewById(R.id.buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        Button mapa = rootView.findViewById(R.id.mapa);
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

        listReserva.setAdapter(new CustomArrayAdapter(rootView.getContext(), listvehiculo));
        listReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(rootView.getContext(), DetallesReserva.class);
                i.putExtra("ve", listvehiculo.get(position));
                i.putExtra("da", listvehiculo.get(position).getFecha());
                i.putExtra("pr", listvehiculo.get(position).getPrecio());
                startActivityForResult(i, 2);
            }
        });
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
            if(listItem == null) listItem = LayoutInflater.from(context).inflate(R.layout.reservalist_layout, parent, false);
            Vehiculo currentVehiculo = vehiculoList.get(position);

            listImage = listItem.findViewById(R.id.listimage);
            DownloadImageTask getImage = new DownloadImageTask();
            //getImage.execute(currentVehiculo.getImagen());
            try {
                listImage.setImageBitmap(getImage.execute(currentVehiculo.getImagen()).get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            listText = listItem.findViewById(R.id.listtext);
            listText.setText("Marca: " + currentVehiculo.getMarca() + "Modelo: " + currentVehiculo.getModelo());
            listText.append("\nFecha: " + currentVehiculo.getFecha());

            return listItem;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmage){

        }

        public Bitmap getBitmapFromURL(String url) {
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
