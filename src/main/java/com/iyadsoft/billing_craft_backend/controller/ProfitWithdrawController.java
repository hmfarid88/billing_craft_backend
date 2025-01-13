package com.iyadsoft.billing_craft_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.ProfitWithdraw;
import com.iyadsoft.billing_craft_backend.repository.ProfitWithdrawRepository;
import com.iyadsoft.billing_craft_backend.service.ProfitWithdrawService;

@RestController
@RequestMapping("/profit")
public class ProfitWithdrawController {
    @Autowired
    private ProfitWithdrawService profitWithdrawService;

    @Autowired
    private ProfitWithdrawRepository profitWithdrawRepository;

    @GetMapping("/current-month-withdrawsum")
    public Double getCurrentMonthProfitWithdraw(@RequestParam String username) {
        return profitWithdrawService.getCurrentMonthWithdrawSum(username);
    }

    @GetMapping("/current-month-depositsum")
    public Double getCurrentMonthProfitDeposit(@RequestParam String username) {
        return profitWithdrawService.getCurrentMonthWithdrawSum(username);
    }

    @GetMapping("/selected-month-withdrawsum")
    public Double getSelectedMonthProfitWithdraw(@RequestParam String username, int year, int month) {
        return profitWithdrawRepository.findCurrentMonthWithdrawSum(username, year, month);
    }

    @GetMapping("/selected-month-depositsum")
    public Double getSelectedMonthDepositsum(@RequestParam String username, int year, int month) {
        return profitWithdrawRepository.findCurrentMonthDepositSum(username, year, month);
    }

    @GetMapping("/profit-withdraws")
    public List<ProfitWithdraw> getProfitWithdrawsByUsername(@RequestParam String username) {
        return profitWithdrawService.getProfitWithdrawsByUsername(username);
    }
}
