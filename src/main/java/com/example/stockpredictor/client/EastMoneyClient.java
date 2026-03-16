package com.example.stockpredictor.client;

import com.example.stockpredictor.model.KLine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EastMoneyClient {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<KLine> fetchDailyKLines(String stockCode, String beginDate, String endDate) {
        String secid = resolveSecId(stockCode);
        String url = "https://push2his.eastmoney.com/api/qt/stock/kline/get"
                + "?secid=" + secid
                + "&fields1=f1,f2,f3,f4,f5,f6"
                + "&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61"
                + "&klt=101"
                + "&fqt=1"
                + "&beg=" + beginDate
                + "&end=" + endDate;

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = MAPPER.readTree(response.body());
            JsonNode dataNode = root.path("data");
            JsonNode klineNode = dataNode.path("klines");
            if (klineNode.isMissingNode() || !klineNode.isArray()) {
                throw new IllegalStateException("No k-line data returned for " + stockCode);
            }

            List<KLine> kLines = new ArrayList<>();
            for (JsonNode item : klineNode) {
                String[] fields = item.asText().split(",");
                if (fields.length < 6) {
                    continue;
                }
                KLine kLine = new KLine(
                        LocalDate.parse(fields[0].trim()),
                        Double.parseDouble(fields[1]),
                        Double.parseDouble(fields[2]),
                        Double.parseDouble(fields[3]),
                        Double.parseDouble(fields[4]),
                        Double.parseDouble(fields[5])
                );
                kLines.add(kLine);
            }
            return kLines;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while fetching k-line data for " + stockCode, e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch k-line data for " + stockCode, e);
        }
    }

    private String resolveSecId(String stockCode) {
        if (stockCode == null || stockCode.isBlank()) {
            throw new IllegalArgumentException("stockCode cannot be blank");
        }
        if (stockCode.startsWith("6")) {
            return "1." + stockCode;
        }
        return "0." + stockCode;
    }
}
