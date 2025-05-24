package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.Expense;
import com.iyadsoft.billing_craft_backend.entity.PaymentRecord;
import com.iyadsoft.billing_craft_backend.entity.SupplierPayment;
import com.iyadsoft.billing_craft_backend.repository.ExpenseRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierPaymentRepository;

@Service
public class TransactionService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    public List<Expense> getLast7DaysExpenses(String username) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        return expenseRepository.findLast7DaysExpensesByUsername(username, sevenDaysAgo);
    }

    public List<PaymentRecord> getLast7DaysOfficePayment(String username) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        return paymentRecordRepository.findLast7DaysOfficePaymentByUsername(username, sevenDaysAgo);
    }

    public List<SupplierPayment> getLast7DaysSupplierPayment(String username) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        return supplierPaymentRepository.findLast7DaysSupplierPaymentByUsername(username, sevenDaysAgo);
    }
}
