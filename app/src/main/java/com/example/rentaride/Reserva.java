package com.example.rentaride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.sundeepk.compactcalendarview.domain.Event;


class Reserva extends Event {
    String IDCoche = "Toyota";

    public Reserva(int color, long timeInMillis, String IDCoche) {
        super(color, timeInMillis);
        this.IDCoche = IDCoche;
    }

    public Reserva(int color, long timeInMillis, Object data, String IDCoche) {
        super(color, timeInMillis, data);
        this.IDCoche = IDCoche;
    }

    public String getIDCoche() {
        return IDCoche;
    }

    public void setIDCoche(String IDCoche) {
        this.IDCoche = IDCoche;
    }
}
