package com.iyadsoft.billing_craft_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.iyadsoft.billing_craft_backend.entity.InvoiceNote;
import com.iyadsoft.billing_craft_backend.entity.ShopInfo;
import com.iyadsoft.billing_craft_backend.service.InvoiceNoteService;
import com.iyadsoft.billing_craft_backend.service.ShopInfoService;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private InvoiceNoteService invoiceNoteService;

    @PutMapping("/addShopInfo")
    public ShopInfo saveOrUpdateShopInfo(@RequestBody ShopInfo shopInfo) {
        return shopInfoService.saveOrUpdateShopInfo(shopInfo);
    }

    @GetMapping("/getShopInfo")
    public Optional<ShopInfo> getShopInfo(@RequestParam String username) {
        return shopInfoService.getShopInfoByUsername(username);
    }

    @PostMapping("/addInvoiceNote")
    public InvoiceNote addInvoiceNote(@RequestBody InvoiceNote invoiceNote) {
        return invoiceNoteService.saveInvoiceNote(invoiceNote);
    }

    @GetMapping("/getInvoiceNote")
    public List<InvoiceNote> getAllInvoiceNotes(@RequestParam String username) {
        return invoiceNoteService.getAllInvoiceNotes(username);
    }

     @DeleteMapping("deleteInvoiceNote/{id}")
    public ResponseEntity<Void> deleteInvoiceNoteById(@PathVariable Long id) {
        invoiceNoteService.deleteInvoiceNoteById(id);
        return ResponseEntity.noContent().build();
    }
}
