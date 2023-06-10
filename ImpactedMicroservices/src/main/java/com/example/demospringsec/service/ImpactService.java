package com.example.demospringsec.service;

import com.example.demospringsec.client.ServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Service
public class ImpactService {

    Logger logger = LoggerFactory.getLogger(ImpactService.class);

    private ServiceClient serviceClient;


    @Autowired
    public ImpactService(ServiceClient serviceClient){
        this.serviceClient = serviceClient;
    }

    public String greeting(){
        return "Welcome to Impacted Services!";
    }

    public List<String> getServices(){
        return serviceClient.getServices();
    }

    public String getTraces(){
        return serviceClient.getTraces();
    }

    public void getTraceIds(){
        serviceClient.getTraceIds();
    }

    public String getDependencies(){
        logger.info("Dependencies: "+serviceClient.getDependencies());
        return serviceClient.getDependencies();
    }

    public LinkedList<Integer> getImpactedServiceIdList(int node){
        return serviceClient.getImpactedServiceIdList(node);
    }

    public LinkedList<String> getImpactedServiceNameList(String node){
        return serviceClient.getImpactedServiceNameList(node);
    }




}
