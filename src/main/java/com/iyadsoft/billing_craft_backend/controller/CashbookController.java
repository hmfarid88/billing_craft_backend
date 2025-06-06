package com.iyadsoft.billing_craft_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.CashbookSaleDto;
import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;
import com.iyadsoft.billing_craft_backend.service.CashBookService;

@RestController
@RequestMapping("/cashbook")
public class CashbookController {
    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private CashBookService cashBookService;

    @GetMapping("/net-sum-before-today")
    public Double getNetSumAmountBeforeToday(@RequestParam String username, @RequestParam LocalDate date) {
        return paymentRecordRepository.findNetSumAmountBeforeToday(username, date);
    }

    @GetMapping("/net-cash-today")
    public Double getNetCashToday(@RequestParam String username) {
        return paymentRecordRepository.findCashToday(username);
    }

    @GetMapping("/allPaymentSum")
    public Double getAllPayment(@RequestParam String username) {
        return paymentRecordRepository.findAllPaymentSum(username);
    }

    @GetMapping("/allReceiveSum")
    public Double getAllReceive(@RequestParam String username) {
        return paymentRecordRepository.findAllReceiveSum(username);
    }

    @GetMapping("/payments/today")
    public List<PaymentDto> getPaymentsForToday(@RequestParam String username, @RequestParam LocalDate date) {
        return cashBookService.getPaymentsForToday(username, date);
    }

    @GetMapping("/receives/today")
    public List<ReceiveDto> getReceivesForToday(@RequestParam String username, @RequestParam LocalDate date) {
        return cashBookService.getReceivesForToday(username, date);
    }

    @GetMapping("/sales/customer")
    public List<CashbookSaleDto> getCustomerSalesDetails(@RequestParam String username, @RequestParam LocalDate date) {
        return cashBookService.getCustomerSalesDetails(username, date);
    }
}
