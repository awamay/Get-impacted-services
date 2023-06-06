package com.pradnesh.zipkin;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import brave.sampler.Sampler;


@SpringBootApplication
public class ZipkinService1Application {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinService1Application.class, args);
	}
	@RestController
	class ZipkinController{
	     
	    @Autowired
	    RestTemplate restTemplate;
	 
	    @Bean
	    public RestTemplate getRestTemplate() {
	        return new RestTemplate();
	    }
	 
	    @Bean
	    public Sampler alwaysSampler() {
	        return Sampler.ALWAYS_SAMPLE;
	    }
	    private final Logger LOG = Logger.getLogger(ZipkinController.class.getName()); 
	    @GetMapping(value="/zipkin1")
	    public String zipkinService1() 
	    {
	    	LOG.info("Inside zipkinService 1...");
	         String response = (String) restTemplate.exchange("http://localhost:8082/zipkin2", 
                        HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
	         
	        return response;
	    }
 }
}
