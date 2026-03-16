package com.example.stockpredictor.service;

import com.example.stockpredictor.model.KLine;
import com.example.stockpredictor.model.PredictionResult;

import java.util.List;

public class SimpleTrendPredictor {

    public PredictionResult predictNextDay(List<KLine> kLines) {
        if (kLines == null || kLines.size() < 30) {
            throw new IllegalArgumentException("At least 30 days of k-line data is required.");
        }

        int n = kLines.size();
        double close = kLines.get(n - 1).getClose();
        double previousClose = kLines.get(n - 2).getClose();

        double ma5 = IndicatorCalculator.movingAverage(kLines, 5, n - 1);
        double ma10 = IndicatorCalculator.movingAverage(kLines, 10, n - 1);
        double ma20 = IndicatorCalculator.movingAverage(kLines, 20, n - 1);
        double rsi14 = IndicatorCalculator.rsi(kLines, 14, n - 1);
        double todayPct = (close - previousClose) / previousClose;

        double vol5 = IndicatorCalculator.volumeAverage(kLines, 5, n - 1);
        double vol5Prev = IndicatorCalculator.volumeAverage(kLines, 5, n - 6);
        double volRatio = vol5Prev > 0 ? (vol5 / vol5Prev) : 1.0;

        double score = 0.0;
        score += (close > ma5) ? 1.0 : -1.0;
        score += (ma5 > ma10) ? 1.0 : -1.0;
        score += (ma10 > ma20) ? 0.5 : -0.5;

        if (rsi14 < 30) {
            score += 0.8;
        } else if (rsi14 > 70) {
            score -= 0.8;
        }

        score += (todayPct > 0) ? 0.3 : -0.3;
        if (volRatio > 1.2 && todayPct > 0) {
            score += 0.4;
        }
        if (volRatio > 1.2 && todayPct < 0) {
            score -= 0.4;
        }

        double upProbability = 1.0 / (1.0 + Math.exp(-score));
        return new PredictionResult(ma5, ma10, ma20, rsi14, upProbability);
    }
}
