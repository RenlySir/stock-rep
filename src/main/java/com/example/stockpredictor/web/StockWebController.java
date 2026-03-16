package com.example.stockpredictor.web;

import com.example.stockpredictor.model.StockAnalysisResponse;
import com.example.stockpredictor.service.StockAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class StockWebController {
    private final StockAnalysisService stockAnalysisService;

    public StockWebController(StockAnalysisService stockAnalysisService) {
        this.stockAnalysisService = stockAnalysisService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("defaultStockCode", "300779");
        model.addAttribute("defaultBeginDate", "20220101");
        model.addAttribute("defaultEndDate", "20991231");
        return "index";
    }

    @GetMapping("api/analyze")
    @ResponseBody
    public ResponseEntity<StockAnalysisResponse> analyze(
            @RequestParam(defaultValue = "300779") String stockCode,
            @RequestParam(defaultValue = "20220101") String beginDate,
            @RequestParam(defaultValue = "20991231") String endDate
    ) {
        try {
            StockAnalysisResponse response = stockAnalysisService.analyze(stockCode, beginDate, endDate);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Analyze failed: " + ex.getMessage(), ex);
        }
    }

    @GetMapping("api/health")
    @ResponseBody
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "ok");
        return status;
    }
}
