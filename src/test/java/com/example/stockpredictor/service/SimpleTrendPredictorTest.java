package com.example.stockpredictor.service;

import com.example.stockpredictor.model.KLine;
import com.example.stockpredictor.model.PredictionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class SimpleTrendPredictorTest {

    @Test
    void shouldReturnProbabilityBetweenZeroAndOne() {
        List<KLine> kLines = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 1, 1);
        for (int i = 0; i < 35; i++) {
            double close = 10 + i * 0.1;
            kLines.add(new KLine(date.plusDays(i), close - 0.2, close, close + 0.3, close - 0.4, 1_000_000 + i * 100));
        }

        SimpleTrendPredictor predictor = new SimpleTrendPredictor();
        PredictionResult result = predictor.predictNextDay(kLines);

        Assertions.assertTrue(result.getUpProbability() >= 0.0);
        Assertions.assertTrue(result.getUpProbability() <= 1.0);
    }
}
