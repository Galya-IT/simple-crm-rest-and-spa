package com.company.crm.exceptions;

public class ClientAlreadyExistsException extends CrmException {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}