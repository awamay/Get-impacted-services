package com.awanika.zipkin.controller;

import brave.sampler.Sampler;
import com.awanika.zipkin.ZipkinService2Application;
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
@RequestMapping("/zipkin2")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("")
    public String zipkinService3()
    {
        LOG.info("From zipkinService 2 to zipkinService 3");

        return restTemplate.exchange("http://localhost:8083/zipkin3/zipkin5",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }
}
