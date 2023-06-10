package com.awanika.zipkin.controller;

import brave.sampler.Sampler;
import com.awanika.zipkin.ZipkinService7Application;
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
@RequestMapping("/zipkin7")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("")
    public String zipkinService4()
    {
        LOG.info("From zipkinService 7 to zipkinService 4");

        return restTemplate.exchange("http://localhost:8084/zipkin4/zipkin5",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

}
