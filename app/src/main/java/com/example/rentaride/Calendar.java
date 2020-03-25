package com.example.rentaride;


import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity {
    List<Event> list = new ArrayList<>();
    CompactCalendarView cv;
    TextView tv;
    AdapterEvento arrayAdapter;
    RecyclerView lv;
    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        cv = findViewById(R.id.calendar);
        lv = findViewById(R.id.list);
        tv = findViewById(R.id.tv);
        recuperar();
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
    }

    private void mostrar(Date fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d LLLL y", Locale.getDefault());
        tv.setText(dateFormat.format(fecha));
        List<Reserva> le = new ArrayList<>();
        for (Event e: cv.getEvents(fecha)
        ) {
            le.add((Reserva) e);
        }
        arrayAdapter = new AdapterEvento(new ArrayList<>(le));
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(arrayAdapter);
    }


    private void recuperar() {
        long day = 86400000;
        list.add(new Reserva(getColor(R.color.C1), new Date().getTime(),"Toyota", 0));
        list.add(new Reserva(getColor(R.color.C2), new Date().getTime(), "Kawasaki", 1));
        list.add(new Reserva(getColor(R.color.C3), new Date().getTime(),"Bicicleta de carretera", 2));
        list.add(new Reserva(getColor(R.color.C1), new Date().getTime()+day,"Nissan", 0));
        list.add(new Reserva(getColor(R.color.C2), new Date().getTime()+day*2, "KTM", 1));
        list.add(new Reserva(getColor(R.color.C3), new Date().getTime()+day*3,"Bicicleta de ciudad", 2));
        cv.addEvents(list);
    }
    public void detalle(View view) {
        startActivity(new Intent(Calendar.this, DetallesReserva.class));
    }

}
