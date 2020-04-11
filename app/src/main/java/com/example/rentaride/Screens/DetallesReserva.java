package com.example.rentaride.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.rentaride.R;
import com.example.rentaride.Logica.Vehiculo;
import com.github.nikartm.button.FitButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.rentaride.Utils.Utils.BICICLETA;
import static com.example.rentaride.Utils.Utils.COCHE;
import static com.example.rentaride.Utils.Utils.MOTOCICLETA;

public class DetallesReserva extends AppCompatActivity {
    Vehiculo v;
    String f;
    double p;
    FitButton fit;

    int typeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_reserva);
        v = (Vehiculo) getIntent().getSerializableExtra("ve");
        p = getIntent().getDoubleExtra("pr", 0);
        long d = getIntent().getLongExtra("da",0);
        fit = findViewById(R.id.eliminar);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        f = s.format(new Date(d));
        typeRequest = getIntent().getIntExtra("ac", 0);
        switch(typeRequest){
            case 0:
                FitButton fb = findViewById(R.id.boton);
                FitButton fb2 = findViewById(R.id.eliminar);
                v.setReservado(true);
                fb.setVisibility(View.GONE);
                fb2.setText("Cancelar Oferta");
                break;
            case 1:
                v.setReservado(true);
                break;
            case 2:
                break;
        }
        addInfo(v);
    }

    public void addInfo(Vehiculo v){
        TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
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
        b9 = findViewById(R.id.fechatv);
        b10 = findViewById(R.id.preciotv);
        t1 = findViewById(R.id.tvpot);
        t2 = findViewById(R.id.tvcom);
        t3 = findViewById(R.id.tvam);
        t4 = findViewById(R.id.tvma);
        t5 = findViewById(R.id.tvtelf);
        imagen = findViewById(R.id.imagen);
        c = findViewById(R.id.carddetalle);

        Glide.with(this)
                .load(v.getImagen())
                .centerCrop()
                .into(imagen);
        switch(v.getType()){
            case COCHE:
                imagen.setBackgroundColor(getColor(R.color.C1));
                c.getBackground().setTint(getColor(R.color.C1));
                b1.setText(v.getMarca() + " " + v.getModelo());
                b2.setText(v.getPotencia());
                b3.setText(v.getCombustible());
                if(v.isAdaptado())b4.setText("Si");
                else b4.setText("No");
                b5.setText(v.getAnyo());
                b6.setText(v.getInfo());
                b9.setText(v.getFecha());
                b10.setText(String.valueOf(p));
                if(v.isReservado()){
                    b7.setText(v.getMatricula());
                    b8.setText(v.getTelefono());
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                    fit.setText("Reservar");
                    fit.setButtonColor(getColor(R.color.pb_blue));
                }
                break;

            case MOTOCICLETA:
                imagen.setBackgroundColor(getColor(R.color.C2));
                c.getBackground().setTint(getColor(R.color.C2));
                b1.setText(v.getMarca() + " " + v.getModelo());
                b2.setText(v.getPotencia());
                b3.setText(v.getCombustible());
                b5.setText(v.getAnyo());
                b6.setText(v.getInfo());
                b9.setText(v.getFecha());
                b10.setText(String.valueOf(p));
                if(v.isReservado()){
                    b7.setText(v.getMatricula());
                    b8.setText(v.getTelefono());
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                    fit.setText("Reservar");
                    fit.setButtonColor(getColor(R.color.pb_blue));
                }
                t3.setVisibility(View.GONE);
                break;

            case BICICLETA:
                imagen.setBackgroundColor(getColor(R.color.C3));
                c.getBackground().setTint(getColor(R.color.C3));
                b1.setText(v.getMarca() + " " + v.getModelo());
                b5.setText(v.getAnyo());
                b6.setText(v.getAnyo());
                b9.setText(v.getFecha());
                b10.setText(String.valueOf(p));
                if(v.isReservado()){
                    b8.setText(v.getTelefono());
                } else {
                    t5.setVisibility(View.GONE);
                    fit.setText("Reservar");
                    fit.setButtonColor(getColor(R.color.pb_blue));
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

    public void mapa(View view){

    }

    public void eliminar(View view) {
        if (typeRequest == 0)
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar oferta")
                    .setMessage("¿Seguro que quiere eliminar esta oferta?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = getIntent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        else
        new AlertDialog.Builder(this)
                .setTitle("Eliminar reserva")
                .setMessage("¿Seguro que quiere eliminar esta reserva?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void ampliar(View view) {
        ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopupWithGlide(v.getImagen());
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) imagePopup.setHideCloseIcon(true);
        imagePopup.setFullScreen(true);
        imagePopup.viewPopup();
    }
}
