package com.jwtly10.loggar.model;

import java.sql.Date;

import lombok.Data;

@Data
public class LogEntry {
    private String message;
    private String level;
    private Date timestamp;
}

