package com.example.demospringsec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraceDetails {
    private String traceId;
    private String id;
    private String kind;
    private String name;
    private String timestamp;
    private String duration;
    private LocalEndpoint localEndpoint;
    private Tags tags;
}
