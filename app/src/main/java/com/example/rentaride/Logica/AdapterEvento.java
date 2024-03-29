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
import java.util.List;

public class AdapterEvento extends RecyclerView.Adapter<AdapterEvento.ViewHolder> {
    private List<Reserva> mDataset;
    private ClickListener clickListener;

    public AdapterEvento(List<Reserva> myDataset) {
        mDataset = myDataset;
        clickListener = new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_event, null);
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tipo, desc;
        CardView cv;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tipo =  itemLayoutView.findViewById(R.id.tipo);
            desc =  itemLayoutView.findViewById(R.id.desc);
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
}
