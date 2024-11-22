package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Bus implements Serializable { // Implementar Serializable
    private String id;
    private String mainImageUrl;
    private List<String> imageUrls; // Lista de URLs para el carrusel
    private double ticketPrice;
    private double subscriptionPrice;

    // Constructor vacío para Firebase
    public Bus() {
    }

    // Constructor
    public Bus(String id, String mainImageUrl, List<String> imageUrls, double ticketPrice, double subscriptionPrice) {
        this.id = id;
        this.mainImageUrl = mainImageUrl;
        this.imageUrls = imageUrls;
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

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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
