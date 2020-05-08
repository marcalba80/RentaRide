package com.example.rentaride.Logica;

public class Message {

    private String message;
    private String senderUid;
    private String senderName;
    private String date;

    public Message(){

    }

    public Message setMessage(String message){
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public Message setSenderUid(String senderUid) {
        this.senderUid = senderUid;
        return this;
    }

    public String getSenderName() {
        return senderName;
    }

    public Message setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Message setDate(String date) {
        this.date = date;
        return this;
    }
}
