package com.example.demospringsec.controller;

import com.example.demospringsec.service.ImpactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ServiceController {

    private final ImpactService impactService;

    @Autowired
    public ServiceController(ImpactService impactService){
        this.impactService = impactService;
    }

    @GetMapping("/home")
    public String getGreeting(){
        return impactService.greeting();
    }

    @GetMapping("/services")
    public List<String> getServices(){
        return impactService.getServices();
    }

    @GetMapping("/traces")
    public String getTraces(){
        return impactService.getTraces();
    }

    @GetMapping("/dependencies")
    public String getDependencies(){
        return impactService.getDependencies();
    }

    @GetMapping("/impacted-service-ids/{node}")
    public LinkedList<Integer> getImpactedServiceIds(@PathVariable int node){
        return impactService.getImpactedServiceIdList(node);
    }

    @GetMapping("/impacted-service-names/{node}")
    public LinkedList<String> getImpactedServices(@PathVariable String node){
        return impactService.getImpactedServiceNameList(node);
    }

}
