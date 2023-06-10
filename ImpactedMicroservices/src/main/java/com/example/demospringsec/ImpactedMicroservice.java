package com.example.demospringsec;

import com.example.demospringsec.client.ServiceClient;
import com.example.demospringsec.service.ImpactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class ImpactedMicroservice implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(ImpactService.class);


    @Autowired
    ImpactService impactService;

    @Autowired
    ServiceClient serviceClient;

    public static void main(String[] args) {
        SpringApplication.run(ImpactedMicroservice.class, args);
    }

    @Override
    public void run(String... args) {
//        impactService.getImpactedList(4);
//        impactService.getDependencies();
        logger.info(String.valueOf(serviceClient.finalSol("zipkin-service-10")));

    }

}