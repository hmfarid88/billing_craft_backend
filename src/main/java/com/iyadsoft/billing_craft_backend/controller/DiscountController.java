package com.iyadsoft.billing_craft_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.DiscountHide;
import com.iyadsoft.billing_craft_backend.repository.DiscountHideRepository;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    @Autowired
    private DiscountHideRepository discountHideRepository;

    @PutMapping("/update-status")
    public DiscountHide updateDiscountStatus(@RequestParam String username, @RequestParam boolean status) {
        Optional<DiscountHide> discountOpt = discountHideRepository.findByUsername(username);
        if (discountOpt.isPresent()) {
            DiscountHide discount = discountOpt.get();
            discount.setStatus(status ? "SHOW" : "HIDE");
            return discountHideRepository.save(discount);
        } else {
            DiscountHide discountHide = new DiscountHide();
            discountHide.setUsername(username);
            discountHide.setStatus("HIDE");
            return discountHideRepository.save(discountHide);
        }

    }

    @GetMapping("/status")
    public DiscountHide getDiscountByUsername(@RequestParam String username) {
        Optional<DiscountHide> discountHide = discountHideRepository.findByUsername(username);
        if (discountHide.isPresent()) {
            return discountHide.get();
        }
        throw new RuntimeException("User not found");
    }
}
