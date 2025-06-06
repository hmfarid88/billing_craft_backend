package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.CashbookSaleDto;
import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.repository.ExpenseRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProfitWithdrawRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierPaymentRepository;

@Service
public class CashBookService {

    private final ExpenseRepository expenseRepository;

    private final PaymentRecordRepository paymentRecordRepository;

    private final SupplierPaymentRepository supplierPaymentRepository;

    private final ProfitWithdrawRepository profitWithdrawRepository;

    private final ProductSaleRepository productSaleRepository;

    @Autowired
    public CashBookService(ProductSaleRepository productSaleRepository, ExpenseRepository expenseRepository,
            PaymentRecordRepository paymentRecordRepository, SupplierPaymentRepository supplierPaymentRepository,
            ProfitWithdrawRepository profitWithdrawRepository) {
        this.productSaleRepository = productSaleRepository;
        this.expenseRepository = expenseRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.profitWithdrawRepository = profitWithdrawRepository;
    }

    public List<PaymentDto> getPaymentsForToday(String username, LocalDate date) {
        List<PaymentDto> userExpense = expenseRepository.findExpenseForToday(username, date);
        List<PaymentDto> userPayments = paymentRecordRepository.findPaymentsForToday(username, date);
        List<PaymentDto> supplierPayments = supplierPaymentRepository.findSupplierPaymentsForToday(username, date);
        List<PaymentDto> profitWithdraw = profitWithdrawRepository.findProfitWithdrawForToday(username, date);
        List<PaymentDto> combinedPayments = new ArrayList<>();
        combinedPayments.addAll(userExpense);
        combinedPayments.addAll(userPayments);
        combinedPayments.addAll(supplierPayments);
        combinedPayments.addAll(profitWithdraw);
        return combinedPayments;
    }

    public List<ReceiveDto> getReceivesForToday(String username, LocalDate date) {
        List<ReceiveDto> userPayments = paymentRecordRepository.findReceivesForToday(username, date);
        List<ReceiveDto> supplierPayments = supplierPaymentRepository.findSupplierReceivesForToday(username, date);
        List<ReceiveDto> profitDeposit = profitWithdrawRepository.findProfitDepositForToday(username, date);
        List<ReceiveDto> combinedPayments = new ArrayList<>();
        combinedPayments.addAll(userPayments);
        combinedPayments.addAll(supplierPayments);
        combinedPayments.addAll(profitDeposit);
        return combinedPayments;
    }

    public List<CashbookSaleDto> getCustomerSalesDetails(String username, LocalDate date) {
        return productSaleRepository.findCustomerSalesDetails(username, date);
    }
}
