package com.company.crm.exceptions;

public class ClientNotFoundException extends CrmException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}