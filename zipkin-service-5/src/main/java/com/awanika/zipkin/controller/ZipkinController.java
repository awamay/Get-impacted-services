package com.awanika.zipkin.controller;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@RestController
@RequestMapping("/zipkin5")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("")
    public String zipkinService8()
    {
        LOG.info("From zipkinService 5 to zipkinService 8");

        return restTemplate.exchange("http://localhost:8088/zipkin8",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

}
