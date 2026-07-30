package com.iyadsoft.billing_craft_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BkashService {
    @Autowired
    private RestTemplate restTemplate;

     public String fetchBkashToken() {
        String url = "https://tokenized.sandbox.bka.sh/v1.2.0-beta/tokenized/checkout/token/grant";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("APP_KEY", "APP_SECRET");

        Map<String, String> body = new HashMap<>();
        body.put("app_key", "YOUR_APP_KEY");
        body.put("app_secret", "YOUR_APP_SECRET");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object token = response.getBody().get("id_token");
            return token != null ? token.toString() : null;
        }
        throw new RuntimeException("Failed to fetch bKash token");
    }

}
