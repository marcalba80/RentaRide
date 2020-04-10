package com.example.rentaride.Screens;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEvento;
import com.example.rentaride.R;
import com.example.rentaride.Logica.Reserva;
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

public class Calendar extends AppCompatActivity {
    List<Event> list = new ArrayList<>();
    CompactCalendarView cv;
    int actual;
    Date last;
    TextView tv;
    List<Reserva> le = new ArrayList<>();
    AdapterEvento arrayAdapter;
    RecyclerView lv;
    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        cv = findViewById(R.id.calendar);
        lv = findViewById(R.id.list);
        tv = findViewById(R.id.tv);
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
                Intent i = new Intent(Calendar.this, DetallesReserva.class);
                actual = list.indexOf(le.get(position));
                i.putExtra("ve", le.get(position).getV());
                startActivityForResult(i, 2);
            }
        });
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(arrayAdapter);
    }


    private void recuperar() {
        long day = 86400000;
        list.add(new Reserva(getColor(R.color.C1), new Date().getTime(),cocheprueba));
        list.add(new Reserva(getColor(R.color.C2), new Date().getTime(), motoprueba));
        list.add(new Reserva(getColor(R.color.C3), new Date().getTime(),biciprueba));
        list.add(new Reserva(getColor(R.color.C1), new Date().getTime()+day,cocheprueba));
        list.add(new Reserva(getColor(R.color.C2), new Date().getTime()+day*2,motoprueba));
        list.add(new Reserva(getColor(R.color.C3), new Date().getTime()+day*3,biciprueba));
        cv.addEvents(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                list.remove(actual);
                cv.removeAllEvents();
                cv.addEvents(list);
                Toast.makeText(Calendar.this, "Se ha eliminado la reserva correctamente", Toast.LENGTH_SHORT).show();
                mostrar(last);
            }
        }
    }
}
