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

    @GetMapping("/current-month-sum")
    public Double getCurrentMonthProfitSum(@RequestParam String username) {
        return profitWithdrawService.getCurrentMonthSum(username);
    }

    @GetMapping("/selected-month-sum")
    public Double getSelectedMonthProfitSum(@RequestParam String username, int year, int month) {
        return profitWithdrawRepository.findCurrentMonthSum(username, year, month);
    }

    @GetMapping("/profit-withdraws")
    public List<ProfitWithdraw> getProfitWithdrawsByUsername(@RequestParam String username) {
        return profitWithdrawService.getProfitWithdrawsByUsername(username);
    }
}
