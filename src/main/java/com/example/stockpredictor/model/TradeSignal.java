package com.example.stockpredictor.model;

public class TradeSignal {
    private final String date;
    private final double price;
    private final String type;
    private final String reason;

    public TradeSignal(String date, double price, String type, String reason) {
        this.date = date;
        this.price = price;
        this.type = type;
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }
}
