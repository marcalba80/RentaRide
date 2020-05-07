package com.example.rentaride.Logica;

public class Tarjeta {
    String numero, año, mes, cvv;

    public Tarjeta() {
    }

    public Tarjeta(String numero, String año, String mes, String cvv) {
        this.numero = numero;
        this.año = año;
        this.mes = mes;
        this.cvv = cvv;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean valid(){
        return numero != null && !numero.equals("") &&
                año != null && !año.equals("") &&
                mes != null && !mes.equals("") &&
                cvv != null && !cvv.equals("");


    }
}
