package com.company.crm.controllers.database;

import org.hibernate.Session;

public class AddNewOperation<T> implements TransactionOperation {
    private T creatableObject;
    
    public AddNewOperation (T object) {
        creatableObject = object;
    }
    
    @Override
    public void execute(Session session) {
        session.save(creatableObject);
    }
}
