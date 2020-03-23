package com.example.rentaride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.sundeepk.compactcalendarview.domain.Event;


class Reserva extends Event {
    String IDCoche = "Toyota";
    int tipo = 0;

    public Reserva(int color, long timeInMillis, String IDCoche, int tipo) {
        super(color, timeInMillis);
        this.IDCoche = IDCoche;
        this.tipo = tipo;
    }

    public Reserva(int color, long timeInMillis, Object data, String IDCoche, int tipo) {
        super(color, timeInMillis, data);
        this.IDCoche = IDCoche;
        this.tipo = tipo;
    }

    public String getIDCoche() {
        return IDCoche;
    }

    public void setIDCoche(String IDCoche) {
        this.IDCoche = IDCoche;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
