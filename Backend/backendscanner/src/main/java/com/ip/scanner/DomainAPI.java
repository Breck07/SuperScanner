package com.ip.scanner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class DomainAPI {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String URL = "http://localhost:8000/domainScan";

    public void callAPI(String domain) throws IOException, InterruptedException {
        String jsonPayload = buildPayload(domain);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.printf("Error %d: %s%n", response.statusCode(), response.body());
            return;
        }

        JsonObject root = GSON.fromJson(response.body(), JsonObject.class);
        
        // Safely extract the inner response object and map it to your Java POJO
        DomainScanResult scanResult = GSON.fromJson(root.get("response"), DomainScanResult.class);
        
        displayResponse(scanResult);
    }

    private static String buildPayload(String domain) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("domain", domain);
        return GSON.toJson(jsonMap);
    }

    private static void displayResponse(DomainScanResult result) {
        System.out.println("========================================");
        System.out.println("DOMAIN SCAN PROFILE: " + result.getDomain().toUpperCase());
        System.out.println("========================================");
        System.out.printf("Is Flagged:  %s%n", result.isFlagged() ? "❌ YES" : "✅ NO");
        System.out.printf("Reputation:  %d%n", result.getReputation());
        System.out.printf("Registrar:   %s%n", result.getRegistrar());
        
        if (result.getStats() != null) {
            System.out.println("\n--- Analysis Stats ---");
            System.out.printf("  Malicious:  %d%n", result.getStats().getMalicious());
            System.out.printf("  Suspicious: %d%n", result.getStats().getSuspicious());
            System.out.printf("  Harmless:   %d%n", result.getStats().getHarmless());
        }

        if (result.getDnsRecords() != null && !result.getDnsRecords().isEmpty()) {
            System.out.println("\n--- DNS Records ---");
            for (DomainScanResult.DnsRecord record : result.getDnsRecords()) {
                System.out.printf("  %-5s -> %s (TTL: %d)%n", 
                        record.getType(), record.getValue(), record.getTtl());
            }
        }
        System.out.println("========================================");
    }
}
