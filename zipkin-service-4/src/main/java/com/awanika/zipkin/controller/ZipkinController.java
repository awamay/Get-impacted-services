package com.awanika.zipkin.controller;

import brave.sampler.Sampler;
import com.awanika.zipkin.ZipkinService4Application;
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
@RequestMapping("/zipkin4")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value="/zipkin5")
    public String zipkinService5()
    {
        LOG.info("From zipkinService 4 to zipkinService 5");

        return restTemplate.exchange("http://localhost:8085/zipkin5",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

    @GetMapping(value="/zipkin6")
    public String zipkinService6()
    {
        LOG.info("From zipkinService 4 to zipkinService 6");

        return restTemplate.exchange("http://localhost:8086/zipkin6",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

}
