package com.example.stockpredictor.model;

import java.time.LocalDate;

public class KLine {
    private final LocalDate date;
    private final double open;
    private final double close;
    private final double high;
    private final double low;
    private final double volume;

    public KLine(LocalDate date, double open, double close, double high, double low, double volume) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getVolume() {
        return volume;
    }
}
