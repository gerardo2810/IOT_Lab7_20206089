package com.example.myapplication;

import java.io.Serializable;

public class Bus implements Serializable { // Implementar Serializable
    private String id;
    private String imageUrl;
    private double ticketPrice;
    private double subscriptionPrice;

    // Constructor vacío para Firebase
    public Bus() {
    }

    // Constructor
    public Bus(String id, String imageUrl, double ticketPrice, double subscriptionPrice) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.ticketPrice = ticketPrice;
        this.subscriptionPrice = subscriptionPrice;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }
}
