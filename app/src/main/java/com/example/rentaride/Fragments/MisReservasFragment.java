package com.example.rentaride.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEvento;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.rentaride.Utils.Utils.biciprueba;
import static com.example.rentaride.Utils.Utils.cocheprueba;
import static com.example.rentaride.Utils.Utils.motoprueba;

public class MisReservasFragment extends Fragment {
    List<Event> list = new ArrayList<>();
    CompactCalendarView cv;
    int actual;
    Date last;
    TextView tv;
    List<Reserva> le = new ArrayList<>();
    AdapterEvento arrayAdapter;
    RecyclerView lv;
    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");

    public MisReservasFragment(){

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
        View v = inflater.inflate(R.layout.calendar, container, false);
        cv = v.findViewById(R.id.calendar);
        lv = v.findViewById(R.id.list);
        tv = v.findViewById(R.id.tv);
        if(list != null && list.isEmpty())recuperar();
        mostrar(new Date());
        cv.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                mostrar(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mostrar(firstDayOfNewMonth);
            }
        });
        return v;
    }

    public void ofertar(View view){

    }

    private void mostrar(Date fecha) {
        last = fecha;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d LLLL y", Locale.getDefault());
        tv.setText(dateFormat.format(fecha));
        le.clear();
        for (Event e: cv.getEvents(fecha)
        ) {
            le.add((Reserva) e);
        }
        arrayAdapter = new AdapterEvento(new ArrayList<>(le));
        arrayAdapter.setOnItemClickListener(new AdapterEvento.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(getContext(), DetallesReserva.class);
                actual = list.indexOf(le.get(position));
                i.putExtra("ve", le.get(position).getV());
                i.putExtra("da", le.get(position).getTimeInMillis());
                i.putExtra("pr", le.get(position).getPrecio());
                i.putExtra("ac", 1);
                startActivityForResult(i, 2);
            }
        });
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(arrayAdapter);
    }


    private void recuperar() {
        long day = 86400000;
        list.add(new Reserva(getResources().getColor(R.color.C1), new Date().getTime(),cocheprueba, 90.5));
        list.add(new Reserva(getResources().getColor(R.color.C2), new Date().getTime(), motoprueba, 20));
        list.add(new Reserva(getResources().getColor(R.color.C3), new Date().getTime(),biciprueba, 10.99));
        list.add(new Reserva(getResources().getColor(R.color.C1), new Date().getTime()+day,cocheprueba, 102.5));
        list.add(new Reserva(getResources().getColor(R.color.C2), new Date().getTime()+day*2,motoprueba, 19.95));
        list.add(new Reserva(getResources().getColor(R.color.C3), new Date().getTime()+day*3,biciprueba, 9.5));
        cv.addEvents(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == -1) {
                list.remove(actual);
                cv.removeAllEvents();
                cv.addEvents(list);
                Toast.makeText(getContext(), "Se ha eliminado la reserva correctamente", Toast.LENGTH_SHORT).show();
                mostrar(last);
            }
        }
    }

}
