package com.example.rentaride.Logica;

import android.location.Location;

import com.github.sundeepk.compactcalendarview.domain.Event;


public class Reserva extends Event {
    Vehiculo v;
    boolean reservado;
    String IDCliente, IDOfertor;
    String telefonoC, telefonoO;
    Location location;

    public Reserva(int color, long timeInMillis, Vehiculo v,Location location, String of, String telefonoO) {
        super(color, timeInMillis);
        this.v = v;
        this.location = location;
        this.IDOfertor = of;
        this.telefonoO = telefonoO;
    }

    public Vehiculo getV() {
        return v;
    }

    public void setV(Vehiculo v) {
        this.v = v;
    }

    public boolean isReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    public void reservar(String id, String telefono){
        this.telefonoC = telefono;
        this.IDCliente = id;
        reservado = true;
    }

    public String getIDCliente() {
        return IDCliente;
    }

    public void setIDCliente(String IDCliente) {
        this.IDCliente = IDCliente;
    }

    public String getIDOfertor() {
        return IDOfertor;
    }

    public void setIDOfertor(String IDOfertor) {
        this.IDOfertor = IDOfertor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTelefonoC() {
        return telefonoC;
    }

    public void setTelefonoC(String telefonoC) {
        this.telefonoC = telefonoC;
    }

    public String getTelefonoO() {
        return telefonoO;
    }

    public void setTelefonoO(String telefonoO) {
        this.telefonoO = telefonoO;
    }
}
