package com.example.rentaride.Logica;

public class Message {
    String message;
    String name;
    long createdAt;
    int type;

    public Message(String message, String name, long createdAt, int type) {
        this.message = message;
        this.name = name;
        this.createdAt = createdAt;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
