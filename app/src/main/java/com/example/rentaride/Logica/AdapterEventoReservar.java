package com.example.rentaride.Logica;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaride.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterEventoReservar extends RecyclerView.Adapter<AdapterEventoReservar.ViewHolder> {
    private List<Reserva> mDataset,copia;
    private ClickListener clickListener;

    public AdapterEventoReservar(List<Reserva> myDataset) {
        mDataset = myDataset;
        clickListener = new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        };
        copia = new ArrayList<>();
        copia.addAll(mDataset);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_event_reservar, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva evento = mDataset.get(position);
        holder.desc.setText(evento.v.marca + " " + evento.v.modelo);
        holder.precio.setText(String.valueOf(evento.getPrecio())+ "€");
        if(evento.v.type == 0){
            holder.tipo.setText(R.string.coche);
            holder.cv.getBackground().setTint(evento.getColor());
        }else if(evento.v.type == 1){

            holder.tipo.setText(R.string.moto);
            holder.cv.getBackground().setTint(evento.getColor());

        }else{
            holder.tipo.setText(R.string.bici);
            holder.cv.getBackground().setTint(evento.getColor());
        }
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        holder.hora.setText(s.format(evento.getTimeInMillis()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hora, tipo, desc, precio;
        CardView cv;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            hora =  itemLayoutView.findViewById(R.id.hora);
            tipo =  itemLayoutView.findViewById(R.id.tipo);
            desc =  itemLayoutView.findViewById(R.id.desc);
            precio =  itemLayoutView.findViewById(R.id.precio);
            cv = itemLayoutView.findViewById(R.id.card);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getPosition(), v);

        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void clear() {
        mDataset.clear();
    }

    public void addAll(List<Reserva> events) {
        mDataset = events;
    }

    public void filtrarMarca(String text) {
        mDataset.clear();
        if (text.isEmpty()) {
            mDataset.addAll(copia);
        } else {
            text = text.toLowerCase();
            for (Reserva item : copia) {
                if (item.v.getMarca().toLowerCase().contains(text)) {
                    mDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarModelo(String text) {
        mDataset.clear();
        if (text.isEmpty()) {
            mDataset.addAll(copia);
        } else {
            text = text.toLowerCase();
            for (Reserva item : copia) {
                if (item.v.getModelo().toLowerCase().contains(text)) {
                    mDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarTipo(int tipo) {
        mDataset.clear();
        for (Reserva item : copia) {
            if (item.v.getType() == tipo) {
                mDataset.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarCombustible(String com) {
        mDataset.clear();
        for (Reserva item : copia) {
            if (item.v.getCombustible().equals(com)) {
                mDataset.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarAdaptado(boolean adaptado) {
        mDataset.clear();
        for (Reserva item : copia) {
            if (item.v.isAdaptado() == adaptado) {
                mDataset.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarFecha(long dia) {
        long day = 86400000;
        mDataset.clear();
        for (Reserva item : copia) {
            if (item.getTimeInMillis() >= dia && item.getTimeInMillis() < (dia+day)) {
                mDataset.add(item);
            }
        }
        notifyDataSetChanged();
    }
}
