package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.ProfitWithdraw;
import com.iyadsoft.billing_craft_backend.repository.ProfitWithdrawRepository;

@Service
public class ProfitWithdrawService {
    @Autowired
    private ProfitWithdrawRepository profitWithdrawRepository;

    public Double getCurrentMonthSum(String username) {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        return profitWithdrawRepository.findCurrentMonthSum(username, currentYear, currentMonth);
    }

    public List<ProfitWithdraw> getProfitWithdrawsByUsername(String username) {
        return profitWithdrawRepository.findByUsername(username);
    }
}
