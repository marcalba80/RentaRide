package com.example.rentaride.Logica;

import com.github.sundeepk.compactcalendarview.domain.Event;


public class Reserva extends Event {
    Vehiculo v;
    double precio;

    public Reserva(int color, long timeInMillis, Vehiculo v, double precio) {
        super(color, timeInMillis);
        this.v = v;
        this.precio = precio;
    }

    public Reserva(int color, long timeInMillis, Object data, Vehiculo v, double precio) {
        super(color, timeInMillis, data);
        this.v = v;
        this.precio = precio;
    }

    public Vehiculo getV() {
        return v;
    }

    public void setV(Vehiculo v) {
        this.v = v;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
