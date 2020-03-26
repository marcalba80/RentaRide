package com.example.rentaride;

public class Vehiculo {
    int type;
    String modelo, año, info, telefono, matricula, potencia, combustible, imagen;
    boolean adaptado, reservado;

    public Vehiculo() {
    }

    public Vehiculo(int type, String modelo, String año, String info, String telefono, String matricula, String potencia, String combustible, String imagen, boolean adaptado) {
        this.type = type;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.telefono = telefono;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.imagen = imagen;
        this.adaptado = adaptado;
    }

    public Vehiculo(int type, String modelo, String año, String info, String telefono, String matricula, String potencia, String combustible, String imagen) {
        this.type = type;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.telefono = telefono;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.imagen = imagen;
    }

    public Vehiculo(int type, String modelo, String año, String info, String telefono, String imagen) {
        this.type = type;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.telefono = telefono;
        this.imagen = imagen;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public boolean isAdaptado() {
        return adaptado;
    }

    public void setAdaptado(boolean adaptado) {
        this.adaptado = adaptado;
    }

    public boolean isReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }
}
