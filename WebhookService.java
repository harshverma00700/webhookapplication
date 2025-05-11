package com.example.webhook.service;

import com.example.webhook.model.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String regNo = "REG12347"; // customize as needed

    public void executeFlow() {
        WebhookResponse response = generateWebhook();
        String sqlQuery = solveSqlProblem(regNo);
        submitFinalQuery(response.getWebhook(), response.getAccessToken(), sqlQuery);
    }

    private WebhookResponse generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = Map.of(
                "name", "John Doe",
                "regNo", regNo,
                "email", "john@example.com"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(url, request, WebhookResponse.class);
        return response.getBody();
    }

    private String solveSqlProblem(String regNo) {
        int lastDigit = Integer.parseInt(regNo.replaceAll("\\D", ""));

        if (lastDigit % 2 == 0) {
            // Even → Question 2
            return "-- Final SQL for Question 2\nSELECT * FROM table_name WHERE condition;";
        } else {
            // Odd → Question 1
            return "-- Final SQL for Question 1\nSELECT * FROM table_name WHERE other_condition;";
        }
    }

    private void submitFinalQuery(String webhookUrl, String accessToken, String sqlQuery) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, String> body = Map.of("finalQuery", sqlQuery);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}
