package com.example.rentaride.Logica;

import com.github.sundeepk.compactcalendarview.domain.Event;


public class Reserva extends Event {
    Vehiculo v;

    public Reserva(int color, long timeInMillis, Vehiculo v) {
        super(color, timeInMillis);
        this.v = v;
    }

    public Reserva(int color, long timeInMillis, Object data, Vehiculo v) {
        super(color, timeInMillis, data);
        this.v = v;
    }

    public Vehiculo getV() {
        return v;
    }

    public void setV(Vehiculo v) {
        this.v = v;
    }

}
