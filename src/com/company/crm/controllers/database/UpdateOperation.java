package com.company.crm.controllers.database;

import org.hibernate.Session;

public class UpdateOperation <T> implements TransactionOperation {
    private T updateableObject;
    
    public UpdateOperation (T object) {
        updateableObject = object;
    }
    
    @Override
    public void execute(Session session) {
        session.update(updateableObject);
    }
}
