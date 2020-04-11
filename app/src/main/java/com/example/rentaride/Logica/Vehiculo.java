package com.example.rentaride.Logica;

import java.io.Serializable;

public class Vehiculo implements Serializable {
    int type;
    private String marca, modelo, anyo, info, telefono, matricula, potencia, combustible, fecha, imagen;
    private boolean adaptado, reservado;
    private double precio;


    public Vehiculo() {
    }

    public Vehiculo(int type,
                    String marca,
                    String modelo,
                    String anyo,
                    String info,
                    String telefono,
                    String matricula,
                    String potencia,
                    String combustible,
                    String fecha,
                    double precio,
                    String imagen,
                    boolean adaptado) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.anyo = anyo;
        this.info = info;
        this.telefono = telefono;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.fecha = fecha;
        this.precio = precio;
        this.imagen = imagen;
        this.adaptado = adaptado;
    }

    public Vehiculo(int type,
                    String marca,
                    String modelo,
                    String anyo,
                    String info,
                    String telefono,
                    String matricula,
                    String potencia,
                    String combustible,
                    String fecha,
                    double precio,
                    String imagen) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.anyo = anyo;
        this.info = info;
        this.telefono = telefono;
        this.matricula = matricula;
        this.potencia = potencia;
        this.combustible = combustible;
        this.fecha = fecha;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Vehiculo(int type,
                    String marca,
                    String modelo,
                    String anyo,
                    String info,
                    String telefono,
                    String fecha,
                    double precio,
                    String imagen) {
        this.type = type;
        this.marca = marca;
        this.modelo = modelo;
        this.anyo = anyo;
        this.info = info;
        this.telefono = telefono;
        this.fecha = fecha;
        this.precio = precio;
        this.imagen = imagen;
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

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setFecha(double precio) {
        this.precio = precio;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }
}
