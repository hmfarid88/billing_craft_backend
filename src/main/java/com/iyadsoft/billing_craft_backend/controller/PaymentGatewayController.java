package com.iyadsoft.billing_craft_backend.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.iyadsoft.billing_craft_backend.service.BkashService;
import com.iyadsoft.billing_craft_backend.service.MissingBill;

@RestController
@RequestMapping("/gateWay")
public class PaymentGatewayController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MissingBill missingBill;

    @Autowired
    BkashService bkashService;

    @PostMapping("/bkash/token")
    public ResponseEntity<?> getBkashToken() {
        String token = bkashService.fetchBkashToken();
        return ResponseEntity.ok(token);
    }

    // @PostMapping("/bkash/create")
    // public ResponseEntity<?> createPayment(@RequestParam String username,
    // @RequestParam BigDecimal amount) {
    // String token = bkashService.fetchBkashToken();

    // String url =
    // "https://tokenized.sandbox.bka.sh/v1.2.0-beta/tokenized/checkout/create";
    // HttpHeaders headers = new HttpHeaders();
    // headers.setBearerAuth(token);
    // headers.set("X-App-Key", "YOUR_APP_KEY");
    // headers.setContentType(MediaType.APPLICATION_JSON);

    // Map<String, Object> paymentRequest = new HashMap<>();
    // paymentRequest.put("amount", amount.toPlainString());
    // paymentRequest.put("currency", "BDT");
    // paymentRequest.put("intent", "sale");
    // paymentRequest.put("merchantInvoiceNumber", UUID.randomUUID().toString());

    // HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest,
    // headers);
    // ResponseEntity<String> response = restTemplate.postForEntity(url, entity,
    // String.class);
    // return response;
    // }
    @PostMapping("/bkash/create")
    public ResponseEntity<?> createPayment(@RequestParam String username, @RequestParam BigDecimal amount) {
        String token = bkashService.fetchBkashToken();

        String url = "https://tokenized.sandbox.bka.sh/v1.2.0-beta/tokenized/checkout/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("X-App-Key", "YOUR_APP_KEY");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String invoiceId = UUID.randomUUID().toString();

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("amount", amount.toPlainString());
        paymentRequest.put("currency", "BDT");
        paymentRequest.put("intent", "sale");
        paymentRequest.put("merchantInvoiceNumber", invoiceId);
        paymentRequest.put("payerReference", username);
        paymentRequest.put("callbackURL", "https://yourdomain.com/payment-success"); 

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            Map<String, Object> result = new HashMap<>();
            result.put("paymentID", body.get("paymentID"));
            result.put("bkashURL", body.get("bkashURL")); // use `paymentExecuteUrl` if needed
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(500).body("bKash payment creation failed");
        }
    }

    @PostMapping("/bkash/execute")
    public ResponseEntity<?> executePayment(@RequestParam String paymentID, @RequestParam String username) {
        String token = bkashService.fetchBkashToken();

        String url = "https://tokenized.sandbox.bka.sh/v1.2.0-beta/tokenized/checkout/execute/" + paymentID;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("X-App-Key", "YOUR_APP_KEY");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 🔄 After success:
        if (response.getStatusCode() == HttpStatus.OK) {
            missingBill.updateBillsAsPaid(username); // See below
            // sendPaymentConfirmationEmail(username);
        }

        return response;
    }

}
