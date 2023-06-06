package com.example.demospringsec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tags {
    private String exception;
    private String httpurl;
    private String method;
    private String outcome;
    private String status;
    private String uri;

}
