package com.jwtly10.loggar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LoggarApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggarApplication.class, args);
    }
}
