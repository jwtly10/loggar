package com.jwtly10.loggar.model;

import lombok.Data;

import java.sql.Date;

@Data
public class LogEntry {
    private String message;
    private String level;
    private Date timestamp;
}
