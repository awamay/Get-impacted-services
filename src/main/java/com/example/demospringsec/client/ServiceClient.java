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
    private HashMap<String,String> idToService = new HashMap<>();
    private LinkedHashMap<String, ArrayList<ArrayList<String>>> serviceToParentId = new LinkedHashMap<>(), serviceToParentName = new LinkedHashMap<>();

    private LinkedHashMap<String, ArrayList<ArrayList<String>>> adjServiceName2 = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedList<String>> adjServiceName = new LinkedHashMap<>();


    @Autowired
    public ServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<String> getServices(){
        return restTemplate.getForObject("http://localhost:9411/api/v2/services", ArrayList.class);
    }

    public List<String> getTraceIds() {
        List<String> traces = new ArrayList<>();
        String response = getTraces();

        logger.info("traces:"+response);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.isArray()) {
                for (JsonNode traceNode : jsonNode) {
//                    traces.add(traceNode.get(0).get("traceId").asText());
                    for(JsonNode spanNode : traceNode){
                        String traceId = spanNode.get("traceId").asText();
                        traces.add(traceId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traces;
    }

    public String getTraces(){
        return restTemplate.getForObject("http://localhost:9411/api/v2/traces", String.class);
    }

    public void getSpanDetails(List<String> traces){
        traces.forEach(traceid -> {
            String res = restTemplate.getForObject("http://localhost:9411/api/v2/trace/"+traceid, String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(res);
                jsonNode.forEach(node -> {
                    if(node.get("kind").asText().equals("SERVER")){
                        String spandId = node.get("id").asText();
                        Optional<JsonNode> parentId = Optional.ofNullable(node.get("parentId"));
                        String serviceName = node.get("localEndpoint").get("serviceName").asText();
                        idToService.put(spandId,serviceName);
                        parentId.ifPresent(s -> serviceToParentId.get(serviceName).add(new ArrayList<>(Arrays.asList(traceid,s.asText()))));
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        serviceToParentId.forEach((k,v) -> {
            v.forEach(p -> {
                serviceToParentName.get(k).add(new ArrayList<>(Arrays.asList(p.get(0),idToService.get(p.get(1)))));
            });
        });

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

    public LinkedList<String> test(){
        ArrayList<String> services = new ArrayList<>(Arrays.asList("a","b","c","d","e","f","g","h","i","j"));
        for(String service : services){
            adjServiceName2.put(service,new ArrayList<>());
            visited.put(service,false);
        }


        adjServiceName2.get("c").add(new ArrayList<>(Arrays.asList("tr1", "a")));
        adjServiceName2.get("c").add(new ArrayList<>(Arrays.asList("tr2", "b")));
        adjServiceName2.get("j").add(new ArrayList<>(Arrays.asList("tr1", "c")));
        adjServiceName2.get("i").add(new ArrayList<>(Arrays.asList("tr1", "j")));
        adjServiceName2.get("e").add(new ArrayList<>(Arrays.asList("tr2", "c")));
        adjServiceName2.get("e").add(new ArrayList<>(Arrays.asList("tr3", "d")));
        adjServiceName2.get("h").add(new ArrayList<>(Arrays.asList("tr2", "e")));
        adjServiceName2.get("h").add(new ArrayList<>(Arrays.asList("tr3", "e")));
        adjServiceName2.get("f").add(new ArrayList<>(Arrays.asList("tr4", "d")));
        adjServiceName2.get("d").add(new ArrayList<>(Arrays.asList("tr3", "g")));

        String ser = "f";
        adjServiceName2.get(ser).forEach(
                v -> {
                    services.forEach(s -> visited.put(s,false));
                    String traceId = v.get(0);
                    String parent = v.get(1);
                    pdfs(parent,traceId);
                }
        );

        return impactedList;

    }

    public LinkedList<String> finalSol(String serviceName){
//        serviceName = "zipkin-service-3";
        List<String> services = getServices();
        for(String service : services){
            serviceToParentId.put(service,new ArrayList<>());
            serviceToParentName.put(service,new ArrayList<>());
            visited.put(service,false);
        }

        getSpanDetails(getTraceIds());
        logger.info("adj map: " + serviceToParentName);

        serviceToParentName.get(serviceName).forEach(
                v -> {
                    services.forEach(s -> visited.put(s,false));
                    String traceId = v.get(0);
                    String parent = v.get(1);
                    pdfs(parent,traceId);
                }
        );

        return impactedList;
    }


    private void pdfs(String service, String traceId) {
        visited.put(service,true);
        if(!impactedList.contains(service))
            impactedList.add(service);

        for(ArrayList<String> v:serviceToParentName.get(service)){
            if(Objects.equals(v.get(0), traceId) && !visited.get(v.get(1))){
//                impactedList.add(v.get(1));
                pdfs(v.get(1),traceId);
            }
        }

    }


}
