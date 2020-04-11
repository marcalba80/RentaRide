package com.example.rentaride.Logica;

import android.location.Location;

import com.github.sundeepk.compactcalendarview.domain.Event;


public class Reserva extends Event {
    Vehiculo v;
    double precio;
    Location location;

    public Reserva(int color, long timeInMillis, Vehiculo v, double precio, Location location) {
        super(color, timeInMillis);
        this.v = v;
        this.precio = precio;
        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
