package com.example.stockpredictor.model;

public class PredictionResult {
    private final double ma5;
    private final double ma10;
    private final double ma20;
    private final double rsi14;
    private final double upProbability;

    public PredictionResult(double ma5, double ma10, double ma20, double rsi14, double upProbability) {
        this.ma5 = ma5;
        this.ma10 = ma10;
        this.ma20 = ma20;
        this.rsi14 = rsi14;
        this.upProbability = upProbability;
    }

    public double getMa5() {
        return ma5;
    }

    public double getMa10() {
        return ma10;
    }

    public double getMa20() {
        return ma20;
    }

    public double getRsi14() {
        return rsi14;
    }

    public double getUpProbability() {
        return upProbability;
    }

    public boolean isUpTomorrow() {
        return upProbability >= 0.5;
    }
}
