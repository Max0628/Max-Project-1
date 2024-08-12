package com.maxchauo.springserver.service.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class PaymentService {
    private static final String PAY_BY_PRIME_URL = "https://sandbox.tappaysdk.com/tpc/payment/pay-by-prime";
    private static final String PARTNER_KEY = "partner_PHgswvYEk4QY6oy3n8X3CwiQCVQmv91ZcFoD5VrkGFXo8N7BFiLUxzeG";
    private static final String MERCHANT_ID = "AppWorksSchool_CTBC";

    public Long processPayment(String prime, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-api-key", PARTNER_KEY);
        String requestJson = """
                {
                    "prime": "%s",
                    "partner_key": "%s",
                    "merchant_id": "%s",
                    "details": "TapPay Test",
                    "amount": %d,
                    "cardholder": {
                        "phone_number": "+886923456789",
                        "name": "Jane Doe",
                        "email": "Jane@Doe.com",
                        "zip_code": "12345",
                        "address": "123 1st Avenue, City, Country",
                        "national_id": "A123456789"
                    },
                    "remember": true
                }
                """.formatted(prime, PARTNER_KEY, MERCHANT_ID, amount);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PAY_BY_PRIME_URL, entity, String.class);
        String responseBody = response.getBody();

        if (responseBody != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                Long status = jsonNode.get("status").asLong();
                return status;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
