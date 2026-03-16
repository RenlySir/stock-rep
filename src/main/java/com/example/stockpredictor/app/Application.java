package com.example.stockpredictor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.stockpredictor")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
