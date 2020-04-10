package com.example.rentaride.Logica;

import com.github.sundeepk.compactcalendarview.domain.Event;


public class Reserva extends Event {
    Vehiculo v;
    String fecha, precio;

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

    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public void setPrecio(String precio){
        this.precio = precio;
    }

    public String getFecha(){
        return this.fecha;
    }

    public String getPrecio(){
        return this.precio;
    }
}
