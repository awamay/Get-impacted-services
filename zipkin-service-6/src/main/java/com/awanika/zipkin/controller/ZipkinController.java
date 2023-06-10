package com.awanika.zipkin.controller;


import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@RestController
@RequestMapping("/zipkin6")
public class ZipkinController {
    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("")
    public String zipkinService1()
    {
        LOG.info("Returning zipkinService 6 value..");

        return "It is a message from Zipkin-Sevice-6";
    }

}
