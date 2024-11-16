package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.InvoiceNote;

public interface InvoiceNoteRepository extends JpaRepository<InvoiceNote, Long>{

    List<InvoiceNote> findByUsername(String username);
    
}
