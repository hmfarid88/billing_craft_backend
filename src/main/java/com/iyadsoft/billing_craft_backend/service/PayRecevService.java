package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
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

    // public List<PayRecevDetails> getPaymentReceiveDetails(String username, String
    // paymentName) {

    // List<PayRecevDetails> payments =
    // paymentRecordRepository.findPaymentsByUserAndPaymentName(username,
    // paymentName);
    // List<PayRecevDetails> receipts =
    // paymentRecordRepository.findReceivesByUserAndPaymentName(username,
    // paymentName);

    // List<PayRecevDetails> combinedDetails = new ArrayList<>();

    // combinedDetails.addAll(payments);
    // combinedDetails.addAll(receipts);

    // combinedDetails.sort(Comparator.comparing(PayRecevDetails::getDate));

    // return combinedDetails;
    // }

    private Double calculateOpeningBalance(
            String username,
            String paymentName,
            LocalDate fromDate) {

        Double payment = paymentRecordRepository.sumPaymentBeforeDate(
                username, paymentName, fromDate);

        Double receive = paymentRecordRepository.sumReceiveBeforeDate(
                username, paymentName, fromDate);

        return value(payment) - value(receive);
    }

    public List<PayRecevDetails> getPaymentReceiveDetails(
            String username,
            String paymentName,
            LocalDate fromDate,
            LocalDate toDate) {

        Double openingBalance = calculateOpeningBalance(username, paymentName, fromDate);

        List<PayRecevDetails> payments = paymentRecordRepository.findPaymentsByUserAndPaymentName(
                username, paymentName, fromDate, toDate);

        List<PayRecevDetails> receipts = paymentRecordRepository.findReceivesByUserAndPaymentName(
                username, paymentName, fromDate, toDate);

        List<PayRecevDetails> list = new ArrayList<>();

        list.addAll(payments);
        list.addAll(receipts);

        list.sort(Comparator.comparing(PayRecevDetails::getDate));

        double balance = openingBalance;

        for (PayRecevDetails dto : list) {
            dto.setOpeningBalance(balance);
            balance += value(dto.getPayment());
            balance -= value(dto.getReceive());

            dto.setBalance(balance);
        }

      
        return list;
    }

    private double value(Double value) {
        return value == null ? 0 : value;
    }
}
