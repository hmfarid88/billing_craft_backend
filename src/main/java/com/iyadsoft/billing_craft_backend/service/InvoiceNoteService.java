package com.iyadsoft.billing_craft_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.InvoiceNote;
import com.iyadsoft.billing_craft_backend.repository.InvoiceNoteRepository;

@Service
public class InvoiceNoteService {
    private final InvoiceNoteRepository invoiceNoteRepository;

    @Autowired
    public InvoiceNoteService(InvoiceNoteRepository invoiceNoteRepository) {
        this.invoiceNoteRepository = invoiceNoteRepository;
    }

    public InvoiceNote saveInvoiceNote(InvoiceNote invoiceNote) {
        return invoiceNoteRepository.save(invoiceNote);
    }

    public List<InvoiceNote> getAllInvoiceNotes(String username) {
        return invoiceNoteRepository.findByUsername(username);
    }

    public void deleteInvoiceNoteById(Long id) {
        invoiceNoteRepository.deleteById(id);
    }
}
