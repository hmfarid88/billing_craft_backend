package com.iyadsoft.billing_craft_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.iyadsoft.billing_craft_backend.entity.WalletName;
import com.iyadsoft.billing_craft_backend.repository.ExpenseRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentNameRepository;
import com.iyadsoft.billing_craft_backend.repository.PaymentRecordRepository;
import com.iyadsoft.billing_craft_backend.repository.ProfitWithdrawRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierPaymentRepository;
import com.iyadsoft.billing_craft_backend.repository.WalletNameRepository;
import com.iyadsoft.billing_craft_backend.service.PayRecevService;
import com.iyadsoft.billing_craft_backend.service.SupplierBalanceService;
import com.iyadsoft.billing_craft_backend.service.TransactionService;

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
    private WalletNameRepository walletNameRepository;

    @Autowired
    private ProfitWithdrawRepository profitWithdrawRepository;

    @Autowired
    private SupplierBalanceService supplierBalanceService;

    @Autowired
    private PayRecevService payRecevService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/addPaymentName")
    public ResponseEntity<?> addPaymentName(@RequestBody PaymentName paymentName) {
        if (paymentNameRepository.existsByUsernameAndPaymentPerson(paymentName.getUsername(),
                paymentName.getPaymentPerson())) {
            throw new DuplicateEntityException("Name " + paymentName.getPaymentPerson() + " is already exists!");
        }
        PaymentName savedName = paymentNameRepository.save(paymentName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedName);
    }

  @PostMapping("/addWalletName")
   public ResponseEntity<?> addOrUpdateWalletName(@RequestBody WalletName walletName) {
    WalletName existingWallet = walletNameRepository.findByUsernameAndWalletName(
        walletName.getUsername(), walletName.getWalletName()
    );

    WalletName savedWallet;
    if (existingWallet != null) {
        existingWallet.setRate(walletName.getRate()); 
        savedWallet = walletNameRepository.save(existingWallet);
        return ResponseEntity.ok(savedWallet);
    } else {
        savedWallet = walletNameRepository.save(walletName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWallet); 
    }
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

    @PostMapping("/walletPayment")
    public PaymentRecord walletPayment(@RequestBody PaymentRecord walletPayment) {
        String paymentType = walletPayment.getPaymentType();
        if (paymentType.equals("payment")) {
            WalletName wallet = walletNameRepository.findByUsernameAndWalletName(walletPayment.getUsername(),
                    walletPayment.getPaymentName());
            double rate = wallet.getRate();
            double amount = walletPayment.getAmount();
            double expenseAmount = (amount * rate) / 100;
            double remainingAmount = amount - expenseAmount;

            Expense expense = new Expense();
            expense.setExpenseName("Wallet Fee of " + wallet.getWalletName());
            expense.setAmount(expenseAmount);
            expense.setDate(walletPayment.getDate());
            expense.setExpenseNote("Auto-calculated from wallet: " + wallet.getWalletName());
            expense.setUsername(walletPayment.getUsername());
            expenseRepository.save(expense);
            walletPayment.setAmount(remainingAmount);

        }
        return paymentRecordRepository.save(walletPayment);
    }

    @PostMapping("/profitWithdraw")
    public ProfitWithdraw newItem(@RequestBody ProfitWithdraw profitWithdraw) {
        return profitWithdrawRepository.save(profitWithdraw);
    }

    @GetMapping("/getPaymentPerson")
    public List<PaymentName> getPaymentNameByUsername(@RequestParam String username) {
        return paymentNameRepository.getPaymentPersonByUsername(username);
    }

    @GetMapping("/getWalletName")
    public List<WalletName> getWalletNameByUsername(@RequestParam String username) {
        return walletNameRepository.getWalletNameByUsername(username);
    }

    @GetMapping("/getPaymentRecord")
    public List<PayRecevBalance> getPaymentRecordByUsername(@RequestParam String username) {
        return paymentRecordRepository.findPayRecevSummaryBalances(username);
    }

    @GetMapping("/getPaymentRecord-details")
    public List<PayRecevDetails> getPaymentRecordDetailsByUsername(@RequestParam String username,
            @RequestParam String paymentName) {
        return payRecevService.getPaymentReceiveDetails(username, paymentName);
    }

    @GetMapping("/getSupplierBalance")
    public List<SupplierSummaryDTO> getSupplierRecordByUsername(@RequestParam String username) {
        return supplierBalanceService.getSupplierData(username);
    }

    @GetMapping("/getSupplierBalance-details")
    public List<SupplierDetailsDto> getSupplierDetailsByUsername(@RequestParam String username,
            @RequestParam String supplierName) {
        return supplierBalanceService.getSupplierDetails(username, supplierName);
    }

    @GetMapping("/getMonthlyExpense")
    public List<Expense> getMonthlyExpenseByUsername(@RequestParam String username) {
        return expenseRepository.findExpenseForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseExpense")
    public List<Expense> getDatewiseExpenseByUsername(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return expenseRepository.findExpenseForDatewise(username, startDate, endDate);
    }

    @GetMapping("/getMonthlyPaymentRecord")
    public List<PaymentRecord> getMonthlyPaymentByUsername(@RequestParam String username) {
        return expenseRepository.findPaymentRecordForCurrentMonth(username);
    }

    @GetMapping("/getDatewisePaymentRecord")
    public List<PaymentRecord> getDatewisePaymentByUsername(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return expenseRepository.findPaymentRecordForDatewise(username, startDate, endDate);
    }

    @GetMapping("/getSelectedSum")
    public Double getSelectedExpenseByUsername(@RequestParam String username, int year, int month) {
        return expenseRepository.findSelectedMonthSum(username, year, month);
    }

    @GetMapping("/getDatewiseExpenseSum")
    public Double getDatewiseExpenseSumByUsername(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return expenseRepository.findDatewiseMonthSum(username, startDate, endDate);
    }

    @GetMapping("/getLast7daysExpense")
    public List<Expense> getLast7ExpenseByUsername(@RequestParam String username) {
        return transactionService.getLast7DaysExpenses(username);
    }

    @GetMapping("/getLast7daysOfficePayment")
    public List<PaymentRecord> getLast7OfficepaymentByUsername(@RequestParam String username) {
        return transactionService.getLast7DaysOfficePayment(username);
    }

    @GetMapping("/getLast7daysSupplierPayment")
    public List<SupplierPayment> getLast7SupplierPaymentByUsername(@RequestParam String username) {
        return transactionService.getLast7DaysSupplierPayment(username);
    }

    @GetMapping("/getExpenseById")
    public ResponseEntity<Expense> getExpenseByUsernameAndId(@RequestParam Long id, @RequestParam String username) {

        return expenseRepository.findByIdAndUsername(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getOfficePayById")
    public ResponseEntity<PaymentRecord> getOfficdePayByUsernameAndId(@RequestParam Long id,
            @RequestParam String username) {

        return paymentRecordRepository.findByIdAndUsername(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getSupplierPayById")
    public ResponseEntity<SupplierPayment> getSupplierPayByUsernameAndId(@RequestParam Long id,
            @RequestParam String username) {

        return supplierPaymentRepository.findByIdAndUsername(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/expense/update/{id}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        return expenseRepository.findById(id)
                .map(existingExpense -> {
                    existingExpense.setDate(updatedExpense.getDate());
                    existingExpense.setExpenseName(updatedExpense.getExpenseName());
                    existingExpense.setExpenseNote(updatedExpense.getExpenseNote());
                    existingExpense.setAmount(updatedExpense.getAmount());

                    Expense saved = expenseRepository.save(existingExpense);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/officepay/update/{id}")
    public ResponseEntity<PaymentRecord> updateOfficepayById(@PathVariable Long id,
            @RequestBody PaymentRecord updatedExpense) {
        return paymentRecordRepository.findById(id)
                .map(existingExpense -> {
                    existingExpense.setDate(updatedExpense.getDate());
                    existingExpense.setPaymentName(updatedExpense.getPaymentName());
                    existingExpense.setPaymentType(updatedExpense.getPaymentType());
                    existingExpense.setPaymentNote(updatedExpense.getPaymentNote());
                    existingExpense.setAmount(updatedExpense.getAmount());

                    PaymentRecord saved = paymentRecordRepository.save(existingExpense);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/supplierpay/update/{id}")
    public ResponseEntity<SupplierPayment> updateSupplierpayById(@PathVariable Long id,
            @RequestBody SupplierPayment updatedExpense) {
        return supplierPaymentRepository.findById(id)
                .map(existingExpense -> {
                    existingExpense.setDate(updatedExpense.getDate());
                    existingExpense.setSupplierName(updatedExpense.getSupplierName());
                    existingExpense.setPaymentType(updatedExpense.getPaymentType());
                    existingExpense.setNote(updatedExpense.getNote());
                    existingExpense.setAmount(updatedExpense.getAmount());

                    SupplierPayment saved = supplierPaymentRepository.save(existingExpense);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteExpenseById/{id}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok("Expense deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found.");
        }
    }

    @DeleteMapping("/deleteOfficepayById/{id}")
    public ResponseEntity<String> deleteOfficepayById(@PathVariable Long id) {
        if (paymentRecordRepository.existsById(id)) {
            paymentRecordRepository.deleteById(id);
            return ResponseEntity.ok("Payment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found. ");
        }
    }

    @DeleteMapping("/deleteSupplierpayById/{id}")
    public ResponseEntity<String> deleteSupplierpayById(@PathVariable Long id) {
        if (supplierPaymentRepository.existsById(id)) {
            supplierPaymentRepository.deleteById(id);
            return ResponseEntity.ok("Payment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found.");
        }
    }

}
