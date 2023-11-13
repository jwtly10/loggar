package com.jwtly10.loggar.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoggingController {

    @PostMapping("/log")
    public String log() {
        return "LoggingController:log()";
    }



    
}
