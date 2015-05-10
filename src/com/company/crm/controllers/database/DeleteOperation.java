package com.company.crm.controllers.database;

import org.hibernate.Session;

public class DeleteOperation<T> implements TransactionOperation {
    private T deletableObject;
    
    public DeleteOperation (T object) {
        deletableObject = object;
    }
    
    @Override
    public void execute(Session session) {
        session.delete(deletableObject);
    }
}
