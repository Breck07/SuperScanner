package com.ip.scanner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NmapAPI {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private String jsonPayload;

    public void callAPI(String targetURL) throws IOException, InterruptedException{
        jsonPayload = buildPayload(targetURL);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8000/nmapScan"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        
        if(response.statusCode() == 200){
            JsonObject root = GSON.fromJson(response.body(), JsonObject.class);
            String nmapResults = root.get("result").getAsString();
            String nmapTarget = root.get("target").getAsString();

            displayResponse(nmapResults, nmapTarget);
        }
    }
    private static String buildPayload(String targetURL){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("target", targetURL);

        return GSON.toJson(jsonMap);
    }
    private static void displayResponse(String nmapResults, String nmapTarget){
        System.out.println("Nmap Results:");
        System.out.println("Target: " + nmapTarget);
        System.out.println("Scan: " + nmapResults);
    }
}
