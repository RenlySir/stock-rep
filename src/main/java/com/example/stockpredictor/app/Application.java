package com.example.stockpredictor.app;

import com.example.stockpredictor.client.EastMoneyClient;
import com.example.stockpredictor.model.KLine;
import com.example.stockpredictor.model.PredictionResult;
import com.example.stockpredictor.service.SimpleTrendPredictor;

import java.util.List;

public class Application {
    public static void main(String[] args) throws Exception {
        String stockCode = args.length > 0 ? args[0] : "300779";
        String beginDate = args.length > 1 ? args[1] : "20220101";
        String endDate = args.length > 2 ? args[2] : "20991231";

        EastMoneyClient client = new EastMoneyClient();
        List<KLine> kLines = client.fetchDailyKLines(stockCode, beginDate, endDate);
        if (kLines.size() < 30) {
            System.out.println("Data size is not enough to predict, need at least 30 daily bars.");
            return;
        }

        SimpleTrendPredictor predictor = new SimpleTrendPredictor();
        PredictionResult prediction = predictor.predictNextDay(kLines);
        KLine latest = kLines.get(kLines.size() - 1);

        System.out.println("Stock code: " + stockCode);
        System.out.println("Latest date: " + latest.getDate());
        System.out.printf("Close price: %.2f%n", latest.getClose());
        System.out.printf("MA5: %.2f, MA10: %.2f, MA20: %.2f%n",
                prediction.getMa5(), prediction.getMa10(), prediction.getMa20());
        System.out.printf("RSI14: %.2f%n", prediction.getRsi14());
        System.out.printf("Probability of up tomorrow: %.2f%%%n", prediction.getUpProbability() * 100.0);
        System.out.println("Predicted trend tomorrow: " + (prediction.isUpTomorrow() ? "UP" : "DOWN"));
    }
}
