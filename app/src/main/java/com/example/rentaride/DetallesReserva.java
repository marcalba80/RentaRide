package com.example.rentaride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.rentaride.Utils.BICICLETA;
import static com.example.rentaride.Utils.COCHE;
import static com.example.rentaride.Utils.MOTOCICLETA;
import static com.example.rentaride.Utils.biciprueba;
import static com.example.rentaride.Utils.cocheprueba;
import static com.example.rentaride.Utils.motoprueba;

public class DetallesReserva extends AppCompatActivity {
    List<Vehiculo> l = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_reserva);
        //switch(tipus intent)
        addInfo(cocheprueba);
    }

    public void addInfo(Vehiculo v){
        TextView b1, b2, b3, b4, b5, b6, b7, b8;
        LinearLayout t1, t2, t3, t4, t5;
        ImageView imagen;
        CardView c;
        b1 = findViewById(R.id.modtv);
        b2 = findViewById(R.id.pottv);
        b3 = findViewById(R.id.comtv);
        b4 = findViewById(R.id.amtv);
        b5 = findViewById(R.id.añotv);
        b6 = findViewById(R.id.infotv);
        b7 = findViewById(R.id.matv);
        b8 = findViewById(R.id.telftv);
        t1 = findViewById(R.id.tvpot);
        t2 = findViewById(R.id.tvcom);
        t3 = findViewById(R.id.tvam);
        t4 = findViewById(R.id.tvma);
        t5 = findViewById(R.id.tvtelf);
        imagen = findViewById(R.id.imagen);
        c = findViewById(R.id.carddetalle);

        Glide.with(this)
                .load(v.imagen)
                .centerCrop()
                .into(imagen);
        switch(v.type){
            case COCHE:
                c.getBackground().setTint(getColor(R.color.C1));
                b1.setText(v.modelo);
                b2.setText(v.potencia);
                b3.setText(v.combustible);
                if(v.adaptado)b4.setText("Si");
                else b4.setText("No");
                b5.setText(v.año);
                b6.setText(v.info);
                if(v.reservado){
                    b7.setText(v.matricula);
                    b8.setText(v.telefono);
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                }
                break;

            case MOTOCICLETA:
                c.getBackground().setTint(getColor(R.color.C2));
                b1.setText(v.modelo);
                b2.setText(v.potencia);
                b3.setText(v.combustible);
                b5.setText(v.año);
                b6.setText(v.info);
                if(v.reservado){
                    b7.setText(v.matricula);
                    b8.setText(v.telefono);
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                }
                t3.setVisibility(View.GONE);
                break;

            case BICICLETA:
                c.getBackground().setTint(getColor(R.color.C3));
                b1.setText(v.modelo);
                b5.setText(v.año);
                b6.setText(v.info);
                if(v.reservado){
                    b8.setText(v.telefono);
                } else {
                    t5.setVisibility(View.GONE);
                }
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);
                t4.setVisibility(View.GONE);
                break;
        }
    }


    public void chat(View view) {
        startActivity(new Intent(DetallesReserva.this, Chat.class));
    }
}