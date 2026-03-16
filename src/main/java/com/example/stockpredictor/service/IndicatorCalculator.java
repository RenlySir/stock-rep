package com.example.stockpredictor.service;

import com.example.stockpredictor.model.KLine;

import java.util.List;

public final class IndicatorCalculator {

    private IndicatorCalculator() {
    }

    public static double movingAverage(List<KLine> kLines, int period, int endIndex) {
        if (endIndex - period + 1 < 0) {
            return Double.NaN;
        }
        double sum = 0;
        for (int i = endIndex - period + 1; i <= endIndex; i++) {
            sum += kLines.get(i).getClose();
        }
        return sum / period;
    }

    public static double volumeAverage(List<KLine> kLines, int period, int endIndex) {
        if (endIndex - period + 1 < 0) {
            return 0;
        }
        double sum = 0;
        for (int i = endIndex - period + 1; i <= endIndex; i++) {
            sum += kLines.get(i).getVolume();
        }
        return sum / period;
    }

    public static double rsi(List<KLine> kLines, int period, int endIndex) {
        if (endIndex - period < 0) {
            return 50.0;
        }
        double gains = 0;
        double losses = 0;
        for (int i = endIndex - period + 1; i <= endIndex; i++) {
            double diff = kLines.get(i).getClose() - kLines.get(i - 1).getClose();
            if (diff >= 0) {
                gains += diff;
            } else {
                losses += -diff;
            }
        }
        double averageGain = gains / period;
        double averageLoss = losses / period;
        if (averageLoss == 0) {
            return 100.0;
        }
        double rs = averageGain / averageLoss;
        return 100.0 - (100.0 / (1.0 + rs));
    }
}
