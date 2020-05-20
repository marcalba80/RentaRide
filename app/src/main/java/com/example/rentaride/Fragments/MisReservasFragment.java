package com.example.rentaride.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.Logica.AdapterEvento;
import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.R;
import com.example.rentaride.Screens.DetallesReserva;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MisReservasFragment extends Fragment {
    List<Event> list = new ArrayList<>();
    CompactCalendarView cv;
    Date last;
    TextView tv;
    private KProgressHUD k;
    List<Reserva> le = new ArrayList<>();
    private Map<Reserva, String> keys = new HashMap<>();
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
        View v = inflater.inflate(R.layout.calendar, container, false);
        cv = v.findViewById(R.id.calendar);
        lv = v.findViewById(R.id.list);
        tv = v.findViewById(R.id.tv);
        last = new Date();
        if(list != null && list.isEmpty())recuperar();
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
                i.putExtra(getString(R.string.otheruid), le.get(position).getIDOfertor());
                i.putExtra(getString(R.string.ownuid), le.get(position).getIDCliente());
                i.putExtra(getString(R.string.keyid),keys.get(le.get(position)));
                i.putExtra(getString(R.string.ve), le.get(position).getV());
                i.putExtra(getString(R.string.da), le.get(position).getTimeInMillis());
                i.putExtra(getString(R.string.telef), le.get(position).getTelefonoO());
                i.putExtra(getString(R.string.lat), le.get(position).getLatitud());
                i.putExtra(getString(R.string.lon), le.get(position).getLongitud());
                i.putExtra(getString(R.string.ac), 1);
                i.putExtra(getString(R.string.reservar), le.get(position).isReservado());
                startActivityForResult(i, 2);
            }
        });
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == -1) {
                recuperar();
                Toast.makeText(getContext(), R.string.reserva_eliminada, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void recuperar(){
        k = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Espere...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        if(!list.isEmpty()){
            list.clear();
            cv.removeAllEvents();
            keys.clear();
            arrayAdapter.clear();
        }
        FirebaseFirestore.getInstance().collection("Reservas").whereEqualTo("reservado", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                Reserva r = document.toObject(Reserva.class);
                                if(r.getIDCliente().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    keys.put(r, document.getId());
                                    list.add(r);
                                }
                            }
                            cv.addEvents(list);
                            mostrar(last);
                            k.dismiss();
                        }
                    }
                });
    }

}
