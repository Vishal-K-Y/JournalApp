package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.api.response.HashResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class HashifyService {
    private static final String API_URL_FOR_POST = "https://api.hashify.net/hash/md5/hex";

    @Autowired
    private RestTemplate restTemplate;

    public HashResponse generateHash(String someText) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userName", someText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setBasicAuth("vishal123", "password123"); // Basic Auth
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make POST request
        ResponseEntity<HashResponse> response = restTemplate.exchange(
                API_URL_FOR_POST, HttpMethod.POST, requestEntity, HashResponse.class
        );
        System.out.println(response.getBody().getDigest());
        return response.getBody();
    }
}
