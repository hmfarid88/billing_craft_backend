package com.iyadsoft.billing_craft_backend.controller;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
