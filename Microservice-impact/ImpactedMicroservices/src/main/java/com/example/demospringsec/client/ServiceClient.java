package com.example.demospringsec.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Component
public class ServiceClient {
    Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private LinkedList<String> impactedList = new LinkedList<>();
    private LinkedHashMap<String,Boolean> visited = new LinkedHashMap<>();

    private LinkedList<Integer> impactedListInt = new LinkedList<>();

    private LinkedList<Integer>[] adjServiceId;

    private LinkedHashMap<String, LinkedList<String>> adjServiceName = new LinkedHashMap<>();

    @Autowired
    public ServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<String> getServices(){
        return restTemplate.getForObject("http://localhost:9411/api/v2/services", ArrayList.class);
    }

    public void getSpans() {
        List<String> traces = new ArrayList<>();
        String response = getTraces();

        logger.info("traces:"+response);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.isArray()) {
                for (JsonNode traceNode : jsonNode) {
                    for (JsonNode spanNode : traceNode) {
                        String traceId = spanNode.get("traceId").asText();
                        traces.add(traceId);
                        String id = spanNode.get("id").asText();
                        String parentid = "null";
                        String parentservice = "null";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTraces(){
        return restTemplate.getForObject("http://localhost:9411/api/v2/traces", String.class);
    }


    public String getDependencies(){
        long currentTimestamp = Instant.now().toEpochMilli();

        // Calculate the start timestamp for the last X minutes
        int x = 30;
        long startTimestamp = currentTimestamp - (x * 60 * 1000);
        String requestUrl = String.format("http://localhost:9411/api/v2/dependencies?startTs=%d&endTs=%d",
                startTimestamp, currentTimestamp);

        String response = restTemplate.getForObject(requestUrl, String.class);

        return response;
    }

    public void setup(){
        List<String> services = getServices();
        adjServiceName = new LinkedHashMap<>();
        visited = new LinkedHashMap<>();

        for(String service : services){
            adjServiceName.put(service,new LinkedList<>());
            visited.put(service,false);
        }

        String dep = getDependencies();

        try {
            JsonNode jsonNode = objectMapper.readTree(dep);
            if (jsonNode.isArray()) {
                for (JsonNode traceNode : jsonNode) {
                    String parent = traceNode.get("parent").asText();
                    String child = traceNode.get("child").asText();
                    adjServiceName.get(child).add(parent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        adjServiceName.get("microservice-b").add("microservice-a");
//        adjServiceName.get("microservice-c").add("microservice-a");
//        adjServiceName.get("microservice-d").add("microservice-c");
//        adjServiceName.get("microservice-d").add("microservice-b");

    }
    public LinkedList<String> getImpactedServiceNameList(String node){
        impactedList = new LinkedList<>();
        setup();

        DFSUtilString(node, visited);

        Collections.sort(impactedList);
        logger.info("list:"+impactedList);

        return impactedList;
    }

    private void DFSUtilString(String v, LinkedHashMap<String,Boolean> visited) {
        visited.put(v,true);
        System.out.print(v + " ");

        // Recur for all the vertices adjacent to this vertex
        for (String n : adjServiceName.get(v)) {
            if (!visited.get(n)) {
                impactedList.add(n);
                DFSUtilString(n, visited);
            }
        }
    }


    //sample service-id graph for demo
    public void serviceIdSetup(int v){
        adjServiceId = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adjServiceId[i] = new LinkedList<Integer>();
        adjServiceId[1].add(0);
        adjServiceId[2].add(0);
        adjServiceId[3].add(2);
        adjServiceId[3].add(1);
        adjServiceId[4].add(3);
    }

    public LinkedList<Integer> getImpactedServiceIdList(int node){
        serviceIdSetup(5);
        impactedListInt = new LinkedList<>();
        boolean[] visited = new boolean[5];

        DFSUtilInt(node, visited);

        Collections.sort(impactedListInt);
        logger.info("list:"+impactedListInt);

        return impactedListInt;
    }

    private void DFSUtilInt(int v, boolean[] visited) {
        visited[v] = true;
        System.out.print(v + " ");

        // Recur for all the vertices adjacent to this vertex
        for (int n : adjServiceId[v]) {
            if (!visited[n]) {
                impactedListInt.add(n);
                DFSUtilInt(n, visited);
            }
        }
    }


}
