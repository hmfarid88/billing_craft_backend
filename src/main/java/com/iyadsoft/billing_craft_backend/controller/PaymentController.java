package com.iyadsoft.billing_craft_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.PayRecevBalance;
import com.iyadsoft.billing_craft_backend.dto.PayRecevDetails;
import com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto;
import com.iyadsoft.billing_craft_backend.dto.SupplierSummaryDTO;
import com.iyadsoft.billing_craft_backend.entity.Expense;
import com.iyadsoft.billing_craft_backend.entity.PaymentName;
import com.iyadsoft.billing_craft_backend.entity.PaymentRecord;
import com.iyadsoft.billing_craft_backend.entity.ProfitWithdraw;
import com.iyadsoft.billing_craft_backend.entity.SupplierPayment;
import com.iyadsoft.billing_craft_backend.repository.ExpenseRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentNameRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;
import com.iyadsoft.billing_craft_backend.repository.ProfitWithdrawRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierPaymentRepository;
import com.iyadsoft.billing_craft_backend.service.SupplierBalanceService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    @Autowired
    private PaymentNameRepository paymentNameRepository;

    @Autowired
    private ProfitWithdrawRepository profitWithdrawRepository;

    @Autowired
    private SupplierBalanceService supplierBalanceService;

    @PostMapping("/addPaymentName")
    public ResponseEntity<?> addPaymentName(@RequestBody PaymentName paymentName) {
        if (paymentNameRepository.existsByUsernameAndPaymentPerson(paymentName.getUsername(),
                paymentName.getPaymentPerson())) {
            throw new DuplicateEntityException("Name " + paymentName.getPaymentPerson() + " is already exists!");
        }
        PaymentName savedName = paymentNameRepository.save(paymentName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedName);
    }

    @PostMapping("/expenseRecord")
    public Expense newExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @PostMapping("/paymentRecord")
    public PaymentRecord newItem(@RequestBody PaymentRecord paymentRecord) {
        return paymentRecordRepository.save(paymentRecord);
    }

    @PostMapping("/supplierPayment")
    public SupplierPayment newItem(@RequestBody SupplierPayment supplierPayment) {
        return supplierPaymentRepository.save(supplierPayment);
    }

    @PostMapping("/profitWithdraw")
    public ProfitWithdraw newItem(@RequestBody ProfitWithdraw profitWithdraw) {
        return profitWithdrawRepository.save(profitWithdraw);
    }

    @GetMapping("/getPaymentPerson")
    public List<PaymentName> getPaymentNameByUsername(@RequestParam String username) {
        return paymentNameRepository.getPaymentPersonByUsername(username);
    }

    @GetMapping("/getPaymentRecord")
    public List<PayRecevBalance> getPaymentRecordByUsername(@RequestParam String username) {
        return paymentRecordRepository.findPayRecevSummaryBalances(username);
    }

    @GetMapping("/getPaymentRecord-details")
    public List<PayRecevDetails> getPaymentRecordDetailsByUsername(@RequestParam String username, @RequestParam String paymentName) {
        return paymentRecordRepository.findDatePaymentReceiveAndNoteByUserAndPaymentName(username, paymentName);
    }

    @GetMapping("/getSupplierBalance")
    public List<SupplierSummaryDTO> getSupplierRecordByUsername(@RequestParam String username) {
        return supplierBalanceService.getSupplierData(username);
    }

    @GetMapping("/getSupplierBalance-details")
    public List<SupplierDetailsDto> getSupplierDetailsByUsername(@RequestParam String username, @RequestParam String supplierName) {
        return supplierBalanceService.getSupplierDetails(username, supplierName);
    }

    @GetMapping("/getMonthlyExpense")
    public List<Expense> getMonthlyExpenseByUsername(@RequestParam String username) {
        return expenseRepository.findExpenseForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseExpense")
    public List<Expense> getDatewiseExpenseByUsername(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findExpenseForDatewise(username, startDate, endDate);
    }

    @GetMapping("/getSelectedSum")
    public Double getSelectedExpenseByUsername(@RequestParam String username, int year, int month) {
        return expenseRepository.findSelectedMonthSum(username, year, month);
    }

    @GetMapping("/getDatewiseExpenseSum")
    public Double getDatewiseExpenseSumByUsername(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findDatewiseMonthSum(username, startDate, endDate);
    }
}
