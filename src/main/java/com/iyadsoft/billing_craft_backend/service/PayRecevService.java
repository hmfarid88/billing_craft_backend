package com.iyadsoft.billing_craft_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.PayRecevDetails;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;

@Service
public class PayRecevService {
    @Autowired
    PaymentRecordRepository paymentRecordRepository;

    public List<PayRecevDetails> getPaymentReceiveDetails(String username, String paymentName) {

        List<PayRecevDetails> payments = paymentRecordRepository.findPaymentsByUserAndPaymentName(username, paymentName);
        List<PayRecevDetails> receipts = paymentRecordRepository.findReceivesByUserAndPaymentName(username, paymentName);

        List<PayRecevDetails> combinedDetails = new ArrayList<>();

        combinedDetails.addAll(payments);
        combinedDetails.addAll(receipts);

        combinedDetails.sort(Comparator.comparing(PayRecevDetails::getDate));

        return combinedDetails;
    }
}
