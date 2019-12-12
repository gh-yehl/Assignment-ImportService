package com.importdata.model;

import java.time.LocalDateTime;

public class PriceDTO {
    private LocalDateTime dateTime;
    private double currentPrice;

    public PriceDTO(LocalDateTime dateTime, double currentPrice) {
        this.dateTime = dateTime;
        this.currentPrice = currentPrice;
    }


    public String getCurrentPrice() {
        String price = String.format("%.2f", currentPrice);
        return price;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
