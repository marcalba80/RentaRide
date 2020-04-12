package com.example.rentaride.Logica;

import android.os.Parcelable;

import java.io.Serializable;

public class Vehiculo implements Serializable {
    int type;
    String marca, modelo, año, info, matricula, potencia, combustible, imagen;
    boolean adaptado;
    double precio;

    public Vehiculo() {
    }

    public Vehiculo(int type, String marca, String modelo, String año, String info, String matricula, String potencia, String combustible, String imagen, boolean adaptado, double precio) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.imagen = imagen;
        this.adaptado = adaptado;
        this.precio = precio;
    }

    public Vehiculo(int type, String marca, String modelo, String año, String info, String matricula, String potencia, String combustible, String imagen, double precio) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.imagen = imagen;
        this.precio = precio;
    }

    public Vehiculo(int type, String marca, String modelo, String año, String info, String imagen, double precio) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.info = info;
        this.imagen = imagen;
        this.precio = precio;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
