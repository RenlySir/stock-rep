package com.example.stockpredictor.service;

import com.example.stockpredictor.client.EastMoneyClient;
import com.example.stockpredictor.model.KLine;
import com.example.stockpredictor.model.PredictionResult;
import com.example.stockpredictor.model.StockAnalysisResponse;
import com.example.stockpredictor.model.TradeSignal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockAnalysisService {
    private final EastMoneyClient eastMoneyClient;
    private final SimpleTrendPredictor simpleTrendPredictor;

    public StockAnalysisService(EastMoneyClient eastMoneyClient, SimpleTrendPredictor simpleTrendPredictor) {
        this.eastMoneyClient = eastMoneyClient;
        this.simpleTrendPredictor = simpleTrendPredictor;
    }

    public StockAnalysisResponse analyze(String stockCode, String beginDate, String endDate) {
        List<KLine> kLines = eastMoneyClient.fetchDailyKLines(stockCode, beginDate, endDate);
        if (kLines.size() < 30) {
            throw new IllegalArgumentException("Need at least 30 trading days to analyze trend.");
        }

        List<String> dates = new ArrayList<>();
        List<Double> closePrices = new ArrayList<>();
        List<Double> ma5Values = new ArrayList<>();
        List<Double> ma20Values = new ArrayList<>();

        for (int i = 0; i < kLines.size(); i++) {
            KLine k = kLines.get(i);
            dates.add(k.getDate().toString());
            closePrices.add(k.getClose());
            ma5Values.add(i >= 4 ? round(IndicatorCalculator.movingAverage(kLines, 5, i)) : null);
            ma20Values.add(i >= 19 ? round(IndicatorCalculator.movingAverage(kLines, 20, i)) : null);
        }

        PredictionResult prediction = simpleTrendPredictor.predictNextDay(kLines);
        List<TradeSignal> signals = buildSignals(kLines);
        String actionSuggestion = toActionSuggestion(prediction.getUpProbability());
        String latestAdvice = buildLatestAdvice(kLines, prediction);

        return new StockAnalysisResponse(
                stockCode,
                dates,
                closePrices,
                ma5Values,
                ma20Values,
                signals,
                prediction.getUpProbability(),
                prediction.isUpTomorrow() ? "UP" : "DOWN",
                actionSuggestion,
                latestAdvice
        );
    }

    private List<TradeSignal> buildSignals(List<KLine> kLines) {
        List<TradeSignal> signals = new ArrayList<>();
        for (int i = 20; i < kLines.size(); i++) {
            double prevClose = kLines.get(i - 1).getClose();
            double curClose = kLines.get(i).getClose();
            double prevMa5 = IndicatorCalculator.movingAverage(kLines, 5, i - 1);
            double curMa5 = IndicatorCalculator.movingAverage(kLines, 5, i);
            double curRsi14 = IndicatorCalculator.rsi(kLines, 14, i);

            if (prevClose <= prevMa5 && curClose > curMa5) {
                signals.add(new TradeSignal(
                        kLines.get(i).getDate().toString(),
                        round(curClose),
                        "BUY",
                        "Close breaks above MA5"
                ));
            } else if (prevClose >= prevMa5 && curClose < curMa5) {
                signals.add(new TradeSignal(
                        kLines.get(i).getDate().toString(),
                        round(curClose),
                        "SELL",
                        "Close falls below MA5"
                ));
            }

            if (curRsi14 < 30) {
                signals.add(new TradeSignal(
                        kLines.get(i).getDate().toString(),
                        round(curClose),
                        "BUY",
                        "RSI14 below 30 (oversold zone)"
                ));
            } else if (curRsi14 > 70) {
                signals.add(new TradeSignal(
                        kLines.get(i).getDate().toString(),
                        round(curClose),
                        "SELL",
                        "RSI14 above 70 (overbought zone)"
                ));
            }
        }

        if (signals.size() <= 20) {
            return signals;
        }
        return signals.subList(signals.size() - 20, signals.size());
    }

    private String toActionSuggestion(double upProbability) {
        if (upProbability >= 0.65) {
            return "Bias to buy on pullback, with position control.";
        }
        if (upProbability >= 0.55) {
            return "Slight bullish edge, wait for confirmation before entry.";
        }
        if (upProbability >= 0.45) {
            return "Neutral zone, stay on the sidelines.";
        }
        if (upProbability >= 0.35) {
            return "Slight bearish edge, reduce exposure.";
        }
        return "Bearish bias, avoid chasing; consider defensive action.";
    }

    private String buildLatestAdvice(List<KLine> kLines, PredictionResult prediction) {
        KLine latest = kLines.get(kLines.size() - 1);
        String trend = prediction.isUpTomorrow() ? "up" : "down";
        return String.format(
                "Latest close %.2f, MA5 %.2f, MA20 %.2f, RSI14 %.2f. Model expects %s move tomorrow (%.2f%%).",
                latest.getClose(),
                prediction.getMa5(),
                prediction.getMa20(),
                prediction.getRsi14(),
                trend,
                prediction.getUpProbability() * 100.0
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
