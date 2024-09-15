package com.iyadsoft.billing_craft_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.ShopInfo;
import com.iyadsoft.billing_craft_backend.service.ShopInfoService;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopInfoService shopInfoService;

     @PutMapping("/addShopInfo")
    public ShopInfo saveOrUpdateShopInfo(@RequestBody ShopInfo shopInfo) {
        return shopInfoService.saveOrUpdateShopInfo(shopInfo);
    }

    @GetMapping("/getShopInfo")
    public Optional<ShopInfo> getShopInfo(@RequestParam String username) {
        return shopInfoService.getShopInfoByUsername(username);
    }
}
