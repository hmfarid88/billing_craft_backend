package com.iyadsoft.billing_craft_backend.service;
import com.iyadsoft.billing_craft_backend.entity.SmsPermission;
import com.iyadsoft.billing_craft_backend.repository.SmsPermissionRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class SmsService {
    private final OkHttpClient client = new OkHttpClient();
    private final SmsPermissionRepository smsPermissionRepository;

    @Value("${bulksmsbd.api.key}")
    private String apiKey;

    @Value("${bulksmsbd.sender.id}")
    private String senderId;

    public SmsService(SmsPermissionRepository smsPermissionRepository) {
        this.smsPermissionRepository = smsPermissionRepository;
    }

    @Transactional
    public String sendSms(String username, String phoneNumber, String message) {
        // Fetch SMS permission details by username
        Optional<SmsPermission> smsPermissionOpt = smsPermissionRepository.findByUsername(username);

        if (smsPermissionOpt.isPresent()) {
            SmsPermission smsPermission = smsPermissionOpt.get();

            // Check if status is "ON" and qty is greater than 0
            if ("ON".equalsIgnoreCase(smsPermission.getStatus()) && smsPermission.getQty() > 0) {

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create("", mediaType);

                // Build the URL with query parameters
                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("bulksmsbd.net")
                        .addPathSegments("api/smsapi")
                        .addQueryParameter("api_key", apiKey)
                        .addQueryParameter("type", "text")
                        .addQueryParameter("number", phoneNumber)
                        .addQueryParameter("senderid", senderId)
                        .addQueryParameter("message", message)
                        .build();

                // Build the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    String responseBody = response.body().string();

                    // Deduct 1 from qty after sending SMS successfully
                    if (response.isSuccessful()) {
                        smsPermission.setQty(smsPermission.getQty() - 1);
                        smsPermissionRepository.save(smsPermission);
                        return "SMS sent successfully! Remaining balance: " + smsPermission.getQty();
                    } else {
                        return "Failed to send SMS: " + responseBody;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error sending SMS: " + e.getMessage();
                }

            } else {
                return "SMS not sent. Either status is OFF or balance is 0.";
            }
        }
        return "User not found.";
    }
}
