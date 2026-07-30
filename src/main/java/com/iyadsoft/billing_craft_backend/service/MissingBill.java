package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.BillStatus;
import com.iyadsoft.billing_craft_backend.entity.UserBill;
import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.UserBillRepository;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;

@Service
public class MissingBill {
    @Autowired
    private UserBillRepository userBillRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public void ensureMissingBills(UserInfo user) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Dhaka")).withDayOfMonth(1);
        LocalDate lastBilled = user.getLastBilledMonth();

        if (lastBilled == null) {
            // fallback: find latest billMonth from user_bills table
            lastBilled = userBillRepository.findLastBillMonth(user.getUsername())
                    .orElse(now.minusMonths(1));
        }

        LocalDate iter = lastBilled.plusMonths(1);
        while (!iter.isAfter(now)) {
            UserBill bill = new UserBill();
            bill.setUsername(user.getUsername());
            bill.setBillMonth(iter);
            bill.setAmount(user.getMonthlyBill());
            bill.setStatus(BillStatus.UNPAID);
            userBillRepository.save(bill);
            iter = iter.plusMonths(1);
        }

        user.setLastBilledMonth(now);
        userInfoRepository.save(user);
    }

    public void updateBillsAsPaid(String username) {
        List<UserBill> unpaidBills = userBillRepository.findByUsernameAndStatus(username, BillStatus.UNPAID);
        LocalDate today = LocalDate.now();

        for (UserBill bill : unpaidBills) {
            bill.setStatus(BillStatus.PAID);
            bill.setPaymentDate(today);
            bill.setPaymentMethod("bKash");
        }

        userBillRepository.saveAll(unpaidBills);
    }

}
