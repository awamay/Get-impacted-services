package com.awanika.zipkin.controller;

import brave.sampler.Sampler;
import com.awanika.zipkin.ZipkinService3Application;
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
@RequestMapping("/zipkin3")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/zipkin5")
    public String zipkinService5()
    {
        LOG.info("From zipkinService 3 to zipkinService 5");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return restTemplate.exchange("http://localhost:8085/zipkin5",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

    @GetMapping("/zipkin9")
    public String zipkinService9()
    {
        LOG.info("From zipkinService 3 to zipkinService 9");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return restTemplate.exchange("http://localhost:8089/zipkin9",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
    }

}
