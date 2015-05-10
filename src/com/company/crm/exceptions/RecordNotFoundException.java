package com.company.crm.exceptions;

public class RecordNotFoundException extends CrmException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}