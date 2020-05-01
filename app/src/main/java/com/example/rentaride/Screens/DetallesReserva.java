package com.example.rentaride.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.rentaride.R;
import com.example.rentaride.Logica.Vehiculo;
import com.github.nikartm.button.FitButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.rentaride.Utils.Utils.BICICLETA;
import static com.example.rentaride.Utils.Utils.COCHE;
import static com.example.rentaride.Utils.Utils.MOTOCICLETA;

public class DetallesReserva extends AppCompatActivity {
    Vehiculo v;
    String f, id;
    int ac;
    double lat, lon;
    FitButton fit, map;
    String mensaje, telefono;
    boolean reservado = false, propia = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_reserva);
        id = getIntent().getStringExtra(getString(R.string.keyid));
        ac = getIntent().getIntExtra(getString(R.string.ac), 0);
        v = (Vehiculo) getIntent().getSerializableExtra(getString(R.string.ve));
        long d = getIntent().getLongExtra(getString(R.string.da),0);
        lat = getIntent().getDoubleExtra(getString(R.string.lat), 0.0);
        lon = getIntent().getDoubleExtra(getString(R.string.lon), 0.0);
        telefono = getIntent().getStringExtra(getString(R.string.telef));
        reservado = getIntent().getBooleanExtra(getString(R.string.reservar), false);
        fit = findViewById(R.id.eliminar);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        f = s.format(new Date(d));
        mensaje = getString(R.string.seguro_eliminar);
        switch(ac){
            case 0:
                FitButton fb = findViewById(R.id.boton);
                FitButton fb2 = findViewById(R.id.eliminar);
                fb.setVisibility(View.GONE);
                propia = true;
                fb2.setText(getString(R.string.cancelar_oferta));
                break;
            case 1:
                break;
            case 2:
                mensaje = getString(R.string.resveh);
                if(getIntent().hasExtra(getString(R.string.map))) {
                    FitButton fb3 = findViewById(R.id.boton2);
                    fb3.setVisibility(View.GONE);
                }
                break;
        }
        addInfo(v);
    }

    public void addInfo(final Vehiculo ve){
        TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11;
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
        map = findViewById(R.id.boton2);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetallesReserva.this, Mapa.class);
                i.putExtra("lat", lat);
                i.putExtra("lon", lon);
                i.putExtra(getString(R.string.tit), ve.getMarca()+" "+ve.getModelo());
                startActivity(i);
            }
        });

        Glide.with(this)
                .load(ve.getImagen())
                .centerCrop()
                .into(imagen);
        switch(ve.getType()){
            case COCHE:
                imagen.setBackgroundColor(getColor(R.color.C1));
                c.getBackground().setTint(getColor(R.color.C1));
                b1.setText(ve.getMarca() + " " + ve.getModelo());
                b2.setText(ve.getPotencia());
                b3.setText(ve.getCombustible());
                if(ve.isAdaptado())b4.setText(R.string.si);
                else b4.setText(R.string.no);
                b5.setText(ve.getAño());
                b6.setText(ve.getInfo());
                b9.setText(f);
                b10.setText(String.valueOf(ve.getPrecio()) + " €");
                if(reservado || propia){
                    if(!reservado) t5.setVisibility(View.GONE);
                    b7.setText(ve.getMatricula());
                    b8.setText(telefono);
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                    fit.setText(getString(R.string.reservar));
                    fit.setButtonColor(getColor(R.color.pb_blue));
                }
                break;

            case MOTOCICLETA:
                imagen.setBackgroundColor(getColor(R.color.C2));
                c.getBackground().setTint(getColor(R.color.C2));
                b1.setText(ve.getMarca() + " " + ve.getModelo());
                b2.setText(ve.getPotencia());
                b3.setText(ve.getCombustible());
                b5.setText(ve.getAño());
                b6.setText(ve.getInfo());
                b9.setText(f);
                b10.setText(String.valueOf(ve.getPrecio()) + " €");
                if(reservado || propia){
                    if(!reservado) t5.setVisibility(View.GONE);
                    b7.setText(ve.getMatricula());
                    b8.setText(telefono);
                } else {
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                    fit.setText(getString(R.string.reservar));
                    fit.setButtonColor(getColor(R.color.pb_blue));
                }
                t3.setVisibility(View.GONE);
                break;

            case BICICLETA:
                imagen.setBackgroundColor(getColor(R.color.C3));
                c.getBackground().setTint(getColor(R.color.C3));
                b1.setText(ve.getMarca() + " " + ve.getModelo());
                b5.setText(ve.getAño());
                b6.setText(ve.getAño());
                b9.setText(f);
                b10.setText(String.valueOf(ve.getPrecio()) + " €");
                if(reservado || propia){
                    if(!reservado) t5.setVisibility(View.GONE);
                    b8.setText(telefono);
                } else {
                    t5.setVisibility(View.GONE);
                    fit.setText(getString(R.string.reservar));
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
        new AlertDialog.Builder(this)
                .setTitle(R.string.at)
                .setMessage(mensaje)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        switch(ac) {
                            case 2:
                                updateDb(true);
                                break;
                            case 1:
                                updateDb(false);
                                break;
                            default:
                                break;
                        }
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void updateDb(boolean reserva) {
        String uid="", telf="";
        if(reserva){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            telf =  PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.preftelefono), "");
        }
        DocumentReference d = FirebaseFirestore.getInstance().collection("Reservas").document(id);
        d.update("reservado", reserva);
        d.update("telefonoC", telf);
        d.update("idcliente", uid);
    }

    public void ampliar(View view) {
        ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopupWithGlide(v.getImagen());
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) imagePopup.setHideCloseIcon(true);
        imagePopup.setFullScreen(true);
        imagePopup.viewPopup();
    }
}
