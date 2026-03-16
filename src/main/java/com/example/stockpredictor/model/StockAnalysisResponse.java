package com.example.stockpredictor.model;

import java.util.List;

public class StockAnalysisResponse {
    private final String stockCode;
    private final List<String> dates;
    private final List<Double> closePrices;
    private final List<Double> ma5;
    private final List<Double> ma20;
    private final List<TradeSignal> signals;
    private final double upProbability;
    private final String trendPrediction;
    private final String actionSuggestion;
    private final String latestAdvice;

    public StockAnalysisResponse(
            String stockCode,
            List<String> dates,
            List<Double> closePrices,
            List<Double> ma5,
            List<Double> ma20,
            List<TradeSignal> signals,
            double upProbability,
            String trendPrediction,
            String actionSuggestion,
            String latestAdvice
    ) {
        this.stockCode = stockCode;
        this.dates = dates;
        this.closePrices = closePrices;
        this.ma5 = ma5;
        this.ma20 = ma20;
        this.signals = signals;
        this.upProbability = upProbability;
        this.trendPrediction = trendPrediction;
        this.actionSuggestion = actionSuggestion;
        this.latestAdvice = latestAdvice;
    }

    public String getStockCode() {
        return stockCode;
    }

    public List<String> getDates() {
        return dates;
    }

    public List<Double> getClosePrices() {
        return closePrices;
    }

    public List<Double> getMa5() {
        return ma5;
    }

    public List<Double> getMa20() {
        return ma20;
    }

    public List<TradeSignal> getSignals() {
        return signals;
    }

    public double getUpProbability() {
        return upProbability;
    }

    public String getTrendPrediction() {
        return trendPrediction;
    }

    public String getActionSuggestion() {
        return actionSuggestion;
    }

    public String getLatestAdvice() {
        return latestAdvice;
    }
}
