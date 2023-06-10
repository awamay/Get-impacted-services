package com.example.demospringsec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalEndpoint {
    private String serviceName;
    private String ipv4;


}
