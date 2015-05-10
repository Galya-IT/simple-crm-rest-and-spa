package com.company.crm.controllers.database;

import org.hibernate.Session;


public interface TransactionOperation {

    void execute(Session session);
    
}
