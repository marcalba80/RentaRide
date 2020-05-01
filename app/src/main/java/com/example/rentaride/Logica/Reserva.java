package com.example.rentaride.Logica;

import android.location.Location;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Objects;


public class Reserva extends Event {
    Vehiculo v;
    boolean reservado;
    String IDCliente, IDOfertor;
    String telefonoC, telefonoO;
    double latitud, longitud;

    public Reserva() {
        super (0, 0);
    }

    public Reserva(int color, long timeInMillis, Vehiculo v,Location location, String of, String telefonoO) {
        super(color, timeInMillis);
        this.v = v;
        this.latitud = location.getLatitude();
        this.longitud = location.getLongitude();
        this.IDOfertor = of;
        this.telefonoO = telefonoO;
    }

    public Reserva(int color, long timeInMillis) {
        super(color, timeInMillis);
    }

    public Reserva(int color, long timeInMillis, Object data) {
        super(color, timeInMillis, data);
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reserva reserva = (Reserva) o;
        return reservado == reserva.reservado &&
                Double.compare(reserva.latitud, latitud) == 0 &&
                Double.compare(reserva.longitud, longitud) == 0 &&
                Objects.equals(v, reserva.v) &&
                Objects.equals(IDCliente, reserva.IDCliente) &&
                Objects.equals(IDOfertor, reserva.IDOfertor) &&
                Objects.equals(telefonoC, reserva.telefonoC) &&
                Objects.equals(telefonoO, reserva.telefonoO);
    }
}
